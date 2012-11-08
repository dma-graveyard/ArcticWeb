// Settings
var initialLat = 56.00;
var initialLon = 11.00;
var initialZoom = 10;
var focusZoom = 10;
var worldZoom = 3;
var minZoomLevel = 1;
var positionUpdate = 20*1000;	// The number of milliseconds between each position update
var listUrl = 'json_proxy/vessel_list';
var detailsUrl = 'json_proxy/vessel_target_details';
var searchUrl = 'json_proxy/vessel_search';

// Global variables
var map;
var vessels = [];
var vesselLayer;
var selectionLayer;
var tracksLayer;
var timeStampsLayer;
var topLeftPixel;
var botRightPixel;
var filterQuery = {};
var showVesselName = false;
var savedTracks;
var savedTimeStamps;
var loadSavedFeatures = false;
var timeOfLastLoad = 0;
var selectedVessel;
var selectedVesselInView = false;
var selectedFeature;
var vesselsResults = [];

/**
 * Sets up the map by adding layers and overwriting 
 * the 'map' element in the HTML index file.
 * Vessels are loaded using JSON and drawn on the map.
 */
function setupMap(){
	
	// Load cookies
	loadView();

	// Create the map and overwrite cotent of the map element
	map = new OpenLayers.Map('map');

	// Get renderer
	var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
	renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
	// renderer = ["Canvas", "SVG", "VML"];
	
	// Create vector layer with a stylemap for vessels
	vesselLayer = new OpenLayers.Layer.Vector(
			"Vessels",
			{
				styleMap: new OpenLayers.StyleMap({
					"default": {
						externalGraphic: "${image}",
						graphicWidth: "${imageWidth}",
						graphicHeight: "${imageHeight}",
						graphicYOffset: "${imageYOffset}",
						graphicXOffset: "${imageXOffset}",
						rotation: "${angle}"
					},
					"select": {
						cursor: "crosshair",
						externalGraphic: "${image}"
					}
				}),
				renderers: renderer
			}
		);

	map.addLayer(vesselLayer);

	// Create vector layer with a stylemap for the selection image
	selectionLayer = new OpenLayers.Layer.Vector(
			"Selection",
			{
				styleMap: new OpenLayers.StyleMap({
					"default": {
						externalGraphic: "${image}",
						graphicWidth: "${imageWidth}",
						graphicHeight: "${imageHeight}",
						graphicYOffset: "${imageYOffset}",
						graphicXOffset: "${imageXOffset}",
						rotation: "${angle}"
					},
					"select": {
						cursor: "crosshair",
						externalGraphic: "${image}"
					}
				}),
				renderers: renderer
			}
		);
		
	map.addLayer(selectionLayer);

	// Create vector layer for past tracks
	tracksLayer = new OpenLayers.Layer.Vector("trackLayer", {
        styleMap: new OpenLayers.StyleMap({'default':{
            strokeColor: "#CC2222",
            strokeOpacity: 1,
            strokeWidth: 3
        }})
    });

	// Create vector layer for time stamps
	timeStampsLayer = new OpenLayers.Layer.Vector("timeStampsLayer", {
        styleMap: new OpenLayers.StyleMap({'default':{
            label : "${timeStamp}",
			fontColor: "#222222",
			fontSize: "11px",
			fontFamily: "'Lucida Grande', Verdana, Geneva, Lucida, Arial, Helvetica, sans-serif",
			fontWeight: "bold",
			labelAlign: "${align}",
			labelXOffset: "${xOffset}",
			labelYOffset: "${yOffset}",
			labelOutlineColor: "#eaeaea",
			labelOutlineWidth: 5,
			labelOutline:1
        }})
    });

	map.addLayer(tracksLayer); 
	map.addControl(new OpenLayers.Control.DrawFeature(tracksLayer, OpenLayers.Handler.Path));  

	map.addLayer(timeStampsLayer); 

	// Add OpenStreetMap Layer
	var osm = new OpenLayers.Layer.OSM(
		"OSM",
		"http://a.tile.openstreetmap.org/${z}/${x}/${y}.png",
		{
			'layers':'basic',
			'isBaseLayer': true
		} 
	);
	
	// Add OpenStreetMap Layer
	map.addLayer(osm);
	// Add KMS Layer
	//addKMSLayer();

	var center = transformPosition(initialLon, initialLat);
	map.setCenter (center, initialZoom);
	
	// Load new vessels with an interval
	setInterval("loadVesselsIfTime()", positionUpdate/10);
	
	setupUI();
	
	//parseFilterQuery(); // FILTER PANEL NOT IN USE

	// Load vessels
	loadVessels();
	
}

/**
 * Loads vessels if time since last update is higher than positionUpdate. 
 */
function loadVesselsIfTime(){
	var timeSinceLastLoad = new Date().getTime() - timeOfLastLoad;
	if (timeOfLastLoad == 0 || timeSinceLastLoad >= positionUpdate){
		timeSinceLastLoad = 0;
		loadVessels();
	}	
}


/**
 * Loads the vessels using JSON and adds each vessel
 * as a vessel instance to the list of vessels.
 * The vessels will be drawn when the JSON is received.
 */
function loadVessels(){

	// Reset list of vessels
	vessels = [];
			
	// Show Loading bar
	$("#loadingBar").css('visibility', 'visible');
	
	// Position loading bar
	var x = $(document).width() / 2 - $("#loadingBar").width() / 2;
	$("#loadingBar").css('left', x);

	// Get points from viewport
	var viewportWidth = $(map.getViewport()).width();
	var viewportHeight = $(map.getViewport()).height();
	topLeftPixel = new OpenLayers.Pixel(0,0);
	botRightPixel = new OpenLayers.Pixel(viewportWidth, viewportHeight);

	var top = map.getLonLatFromPixel(topLeftPixel).transform(
			map.getProjectionObject(), // from Spherical Mercator Projection
			new OpenLayers.Projection("EPSG:4326") // to WGS 1984
		);
	var bot = map.getLonLatFromPixel(botRightPixel).transform(
			map.getProjectionObject(), // from Spherical Mercator Projection
			new OpenLayers.Projection("EPSG:4326") // to WGS 1984
		);
		
	//console.log("Requesting vessels from: " + top.lon + " " + top.lat + " to " + bot.lon + " " + bot.lat);
	
	filterQuery.topLon = top.lon; 
	filterQuery.topLat = top.lat; 
	filterQuery.botLon = bot.lon; 
	filterQuery.botLat = bot.lat; 
				
	$.getJSON(listUrl, 
			filterQuery, 
			function (result) {
			
		// Update vessel counter
		$("#vesselsWorld").html(result.vesselsInWorld);

		// Load new vessels
		var JSONVessels = result.vesselList.vessels;
		
		for (vesselId in JSONVessels) {
			// Create vessel based on JSON data
			var vesselJSON = JSONVessels[vesselId];
			var vessel = new Vessel(vesselId, vesselJSON, 1);
			
			if (vessels[vesselId]) {
				// Vessel exists just update data
				vessels[vesselId] = vessel;
			} else {
				vessels.push(vessel);
			}
			
		}
		
		// Draw vessels
		drawVessels();

		// Hide Loading bar
		$("#loadingBar").css('visibility', 'hidden');
		
		// Set time of load
		timeOfLastLoad = new Date().getTime();
	});
}


/**
 * Draws all known vessels using vector points styled to show images.
 * Vessels are drawn based on their color, angle and whether they are
 * moored on not.
 */
function drawVessels(){

	var vesselFeatures = [];
	var selectionFeatures = [];
	selectedVesselInView = false;

	// Draw all vessels or search results
	vesselsToDraw = vessels;
	if (map.zoom < minZoomLevel){
		vesselsToDraw = vesselsResults;
	}

	// Update number of vessels
	$("#vesselsView").html(""+vesselsToDraw.length);
	
	// Iterate through vessels where value refers to each vessel.
	$.each(vesselsToDraw, function(key, value) { 
		
		// Use styled vector points
		var feature = new OpenLayers.Feature.Vector(
			new OpenLayers.Geometry.Point( value.lon , value.lat ).transform(
				new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
				map.getProjectionObject() // to Spherical Mercator Projection
			),
		 	{	
				id: value.id,
				angle: value.degree - 90, 
				opacity:1, 
				image:"img/" + value.image,
				imageWidth: value.imageWidth,
				imageHeight: value.imageHeight,
				imageYOffset: value.imageYOffset,
				imageXOffset: value.imageXOffset,
				isVessel: true,
				vessel: value
			}
		);
		
		vesselFeatures.push(feature);

		// Set vessel in focus if searched
		if (selectedVessel && value.id == selectedVessel.id){

			selectedVesselInView = true;

			// Update selected vessel
			selectedVessel = value;
			selectedFeature = feature;

			// Update vessel details
			updateVesselDetails(feature);

		}
	});

	var selectionFeature;

	// Set search result in focus
	if (selectedFeature && selectedVesselInView){
		selectionFeature = new OpenLayers.Feature.Vector(
			new OpenLayers.Geometry.Point( selectedFeature.geometry.x , selectedFeature.geometry.y ),
		 	{	
				id: -1,
				angle: selectedFeature.attributes.angle - 90, 
				opacity:1, 
				image:"img/selection.png",
				imageWidth: 32,
				imageHeight: 32,
				imageYOffset: -16,
				imageXOffset: -16,
				isVessel: false
			}
		);
		
		selectionFeatures.push(selectionFeature);

	}

	// Redraw vessels and selection
	vesselLayer.removeAllFeatures();
	selectionLayer.removeAllFeatures();
	vesselLayer.addFeatures(vesselFeatures);
	selectionLayer.addFeatures(selectionFeatures);
	vesselLayer.redraw();
	selectionLayer.redraw();
	drawPastTrack(null);

}

/**
 * Redraws all features in vessel layer and selection layer.
 * Features are vessels.
 */
function redrawSelection(){
	var selectionFeature;
	var selectionFeatures = [];
	drawPastTrack(null);

	// Set search result in focus
	if (selectedFeature){
		selectionFeature = new OpenLayers.Feature.Vector(
			new OpenLayers.Geometry.Point( selectedFeature.geometry.x , selectedFeature.geometry.y ),
		 	{	
				id: -1,
				angle: selectedFeature.attributes.angle - 90, 
				opacity:1, 
				image:"img/selection.png",
				imageWidth: 32,
				imageHeight: 32,
				imageYOffset: -16,
				imageXOffset: -16,
				isVessel: false
			}
		);
		
		selectionFeatures.push(selectionFeature);
		selectedVesselInView = true;
		updateVesselDetails(selectedFeature);

	}

	selectionLayer.removeAllFeatures();
	selectionLayer.addFeatures(selectionFeatures);
	selectionLayer.redraw();
}

/**
 * Draws the past track.
 * If tracks are null, it will simply remove all tracks and draw nothing.
 *
 * @param tracks 
 *		Array of tracks
 */
function drawPastTrack(tracks) {

	// Remove old tracks
	tracksLayer.removeAllFeatures();
	timeStampsLayer.removeAllFeatures();

	// Draw tracks
	if (selectedVesselInView && tracks){
		var lastLon;
		var lastLat;
		var firstPoint = true;
		var untilTimeStamp = 0;
		var tracksBetweenTimeStamps = 80;

		for(track in tracks) {
			var currentTrack = tracks[track];
			if (!firstPoint){
				// Insert line
				var points = new Array(
					new OpenLayers.Geometry.Point(lastLon, lastLat).transform(
							new OpenLayers.Projection("EPSG:4326"), 
							map.getProjectionObject()),
					new OpenLayers.Geometry.Point(currentTrack.lon, currentTrack.lat).transform(
							new OpenLayers.Projection("EPSG:4326"), 
							map.getProjectionObject())
				);
			
				var line = new OpenLayers.Geometry.LineString(points);
				var lineFeature = new OpenLayers.Feature.Vector(line);
				tracksLayer.addFeatures([lineFeature]);

				// Insert timeStamp?
				if (untilTimeStamp == 0 && parseInt(track) + 10 < tracks.length){

					var timeStampPos = points[0];
					var timeStampFeature = new OpenLayers.Feature.Vector(timeStampPos);

					var time = 	currentTrack.time.split(" ")[3] + " " +
								currentTrack.time.split(" ")[4];

					timeStampFeature.attributes = {timeStamp: time};
					timeStampsLayer.addFeatures([timeStampFeature]);

					untilTimeStamp = tracksBetweenTimeStamps;

				} else {
					untilTimeStamp --;
				}
			}
			lastLon = currentTrack.lon;
			lastLat = currentTrack.lat;	
			firstPoint = false;
		}
	
		// Draw features
		tracksLayer.refresh();
		timeStampsLayer.refresh();
	}
}

/**
 * Searches for the vessel described in the search field.
 */
function search(){
	// Read search field
	var arg = $("#searchField").val();
	
	if (arg.length > 0){

		// Show loader
		$("#searchLoad").css('visibility', 'visible');

		// Load search results
		$.getJSON(searchUrl, { argument: arg }, function (result) {
				var s = "s";
				
				// Show search results
				$("#searchResults").css('visibility', 'visible');
					
				// Search results
				vesselsResults = [];

				// Get vessels
				for (vesselId in result.vessels) {
					var vesselJSON = result.vessels[vesselId];
					var vessel = new Vessel(vesselId, vesselJSON, 1);
					vesselsResults.push(vessel);
				}

				// Focus on vessel
				if (vesselsResults.length == 1){
					s = "";
					selectedVessel = vesselsResults[0];
					var center = new OpenLayers.LonLat(selectedVessel.lon, selectedVessel.lat).transform(
						new OpenLayers.Projection("EPSG:4326"), 
						map.getProjectionObject()
						);
					map.setCenter (center, focusZoom);
				} else {
					//map.zoomTo(worldZoom);
				}

				$("#searchMatch").html(result.vesselCount + " vessel" + s + " match.");

				// Hide loader
				$("#searchLoad").css('visibility', 'hidden');

			});
	} else {
		vesselsResults = [];
		drawVessels();

		// Hide results
		$("#searchMatch").html('');
		$("#searchResults").css('visibility', 'hidden');
	}

}


/**
 * Transforms a position to a position that can be used 
 * by OpenLayers. The transformation uses 
 * OpenLayers.Projection("EPSG:4326").
 * 
 * @param lon
 *            The longitude of the position to transform
 * @param lat
 *            The latitude of the position to transform
 * @returns The transformed position as a OpenLayers.LonLat 
 * instance.
 */
function transformPosition(lon, lat){
	return new OpenLayers.LonLat( lon , lat )
		.transform(
			new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
			map.getProjectionObject() // to Spherical Mercator Projection
		);
}


/**
 * Resets the filterQuery object and adds a filter preset.
 * 
 * @param presetSelect
	 	A string in the following form:
 * 		"key = value"
 *
 */
function useFilterPreset(presetSelect) {
	
	resetFilterQuery();

	filter = presetSelect.options[presetSelect.selectedIndex].value;
	if (filter != ""){
		var expr = 'filterQuery.' + filter.split("=")[0] + ' = "' + filter.split("=")[1] + '"';
		eval(expr);
	}
	
	parseFilterQuery();			
	filterChanged();
}


/**
 * Resets the filterQuery.
 */
function resetFilterQuery(){
	delete filterQuery.country;
	delete filterQuery.sourceCountry;
	delete filterQuery.sourceType;
	delete filterQuery.sourceRegion;
	delete filterQuery.sourceBS;
	delete filterQuery.sourceSystem;
	delete filterQuery.vesselClass;
}


/**
 * Clears the values in the filter panel.
 */
function clearFilters() {
	$("#country").val("");
	$("#coruceCountry").val("");
	$("#courceType").val("");
	$("#sourceRegion").val("");
	$("#sourceBS").val("");
	$("#sourceSystem").val("");
	$("#vesselClass").val("");
}


/**
 * Sets the values in the filter panel equal to 
 * the values of the filterQuery object.
 */
function parseFilterQuery() {
	clearFilters();
	$("#country").val(filterQuery.country);
	$("#soruceCountry").val(filterQuery.sourceCountry);
	$("#sourceType").val(filterQuery.sourceType);
	$("#sourceRegion").val(filterQuery.sourceRegion);
	$("#sourceBS").val(filterQuery.sourceBS);
	$("#sourceSystem").val(filterQuery.sourceSystem);
	$("#vesselClass").val(filterQuery.vesselClass);
}


/**
 * Applys the values of the filter panel to the 
 * filterQuery object.
 */
function applyFilter() {
	resetFilterQuery();
	
	filterQuery.country = $("#country").val();
	filterQuery.sourceCountry = $("#soruceCountry").val();
	filterQuery.sourceType = $("#sourceType").val();
	filterQuery.sourceRegion = $("#sourceRegion").val();
	filterQuery.sourceBS = $("#sourceBS").val();
	filterQuery.sourceSystem = $("#sourceSystem").val();
	filterQuery.vesselClass = $("#vesselClass").val();

	filterChanged();
}


/**
 * Method for refreshing when filtering is changed.
 */
function filterChanged() {
	// Save query cookie
	var f = JSON.stringify(filterQuery)
	setCookie("dma-ais-query", f, 30);
	
    loadVessels();
}


/**
 * Method for saving the current view into a cookie.
 */
function saveViewCookie() {
	var center = map.getCenter();
	setCookie("dma-ais-zoom", map.zoom, 30);
	var lonlat = new OpenLayers.LonLat(map.center.lon, map.center.lat).transform(
		map.getProjectionObject(), // from Spherical Mercator Projection
		new OpenLayers.Projection("EPSG:4326") // to WGS 1984
	); 
	setCookie("dma-ais-lat", lonlat.lat, 30);
	setCookie("dma-ais-lon", lonlat.lon, 30);	
}


/**
 * Method for saving the current view into a cookie.
 */
function loadView() {
	var zoom = getCookie("dma-ais-zoom");
	var lat = getCookie("dma-ais-lat");
	var lon = getCookie("dma-ais-lon");
	var q = getCookie("dma-ais-query");
	if (zoom) {
		initialZoom = parseInt(zoom);
	}
	if (lat && lon) {
		initialLat = parseFloat(lat);
		initialLon = parseFloat(lon);
	}
	if (q) {
		eval("filterQuery = " + q + ";");
	}
}


/**
 * Method for setting a cookie.
 */
function setCookie(c_name,value,exdays) {
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
	document.cookie=c_name + "=" + c_value;
}


/**
 * Method for getting a cookie.
 */
function getCookie(c_name) {
	var i,x,y,ARRcookies=document.cookie.split(";");
	for (i=0;i<ARRcookies.length;i++) {
		x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
		y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
		x=x.replace(/^\s+|\s+$/g,"");
		if (x==c_name) {
			return unescape(y);
		}
	}
}
