// Global variables
var detailsOpen = false;
var legendsOpen = false;
var filteringOpen = false;
var searchOpen = false;
var detailsReadyToClose = false;
var selectControl;
var hoverControl;

/**
 * Sets up the panels, event listeners and selection controllers.
 */
function setupUI(){

	// Remove zoom panel
	$(".olControlZoom").css('visibility', 'hidden');

	// Position loading bar
	var x = $(document).width() / 2 - $("#loadingBar").width() / 2;
	$("#loadingBar").css('left', x);

	// Update mouse location when moved
	map.events.register("mousemove", map, function(e) { 
		var position = this.events.getMousePosition(e);
		pixel = new OpenLayers.Pixel(position.x, position.y);
		var lonLat = map.getLonLatFromPixel(pixel).transform(
			map.getProjectionObject(), // from Spherical Mercator Projection
			new OpenLayers.Projection("EPSG:4326") // to WGS 1984
		);
		$("#location").html(lonLat.lon.toFixed(4) + ", " + lonLat.lat.toFixed(4));
	});
	
	// Create functions for hovering a vessel
	var showName = function(e) {
		if (e.feature.attributes.vessel){
			$.getJSON(detailsUrl, {
				past_track: '1',
				id: e.feature.attributes.id
			}, function(result) {
				var pointVessel = new OpenLayers.Geometry.Point(e.feature.geometry.x, e.feature.geometry.y);
				var lonlatVessel = new OpenLayers.LonLat(pointVessel.x, pointVessel.y);
				var pixelVessel = map.getPixelFromLonLat(lonlatVessel);
				var pixelTopLeft = new OpenLayers.Pixel(0,0);
				var lonlatTopLeft = map.getLonLatFromPixel(pixelTopLeft)
				pixelTopLeft = map.getPixelFromLonLat(lonlatTopLeft);

				var x = pixel.x - pixelTopLeft.x;
				var y = pixel.y - pixelTopLeft.y;

				$("#vesselNameBox").html(result.name);
				$("#vesselNameBox").css('visibility', 'visible');
				$("#vesselNameBox").css('top', (y - 26) + 'px');
				$("#vesselNameBox").css('left', x + 'px');
			});
		}
	};

	var hideName = function(e) {
    	$("#vesselNameBox").css('visibility', 'hidden');
    };

	// Create hover control
	hoverControl = new OpenLayers.Control.SelectFeature(vesselLayer, 
		{	
			hover: true,
                highlightOnly: true,
                //renderIntent: "temporary",
                eventListeners: {
                   	featurehighlighted: showName,
                   	featureunhighlighted: hideName
                }
		}
	);

	// Create select control
	selectControl = new OpenLayers.Control.SelectFeature(vesselLayer, 
		{	
			clickout: true, 
			toggle: true,
			onSelect: function(feature) {
				if (selectedVessel && selectedVessel.id == feature.attributes.vessel.id){
					selectedFeature = null;
					selectedVessel = null;
					detailsReadyToClose = true;
					$("#vesselNameBox").css('visibility', 'hidden');
					redrawSelection();
					selectControl.unselectAll();
				} else {
					selectedFeature = feature;
					selectedVessel = feature.attributes.vessel;
					updateVesselDetails(feature);
					$("#vesselNameBox").css('visibility', 'hidden');
					redrawSelection();
				}
			},
			onUnselect: function(feature) {
				selectedFeature = null;
				selectedVessel = null;
				detailsReadyToClose = true;
				tracksLayer.removeAllFeatures();
				timeStampsLayer.removeAllFeatures();
				redrawSelection();
				selectControl.unselectAll();
			}
		}
	);
	
	// Add select controller to map and activate
	map.addControl(hoverControl);
    map.addControl(selectControl);
	hoverControl.activate();    
	selectControl.activate();

	// Register listeners
	map.events.register("movestart", map, function() {
		
    });
	map.events.register("moveend", map, function() {
        loadVessels();
		saveViewCookie();
		$("#vesselNameBox").css('visibility', 'hidden');
		var clear = false;
		if (map.zoom < minZoomLevel){
			detailsReadyToClose = true;
			selectControl.unselectAll();
			tracksLayer.removeAllFeatures();
			timeStampsLayer.removeAllFeatures();
			if (vesselsResults.length == 0){
				vesselLayer.removeAllFeatures();
			}
			
		}
    });
	
	// Set click events on vessel details panel
	$("#detailsHeader").click(function() {
		if (detailsOpen){
			$("#detailsContainer").slideUp(
				{
					complete:function(){
						$("#detailsHeader").html("Vessel details");
						detailsOpen = false;
						$("#detailsPanel").removeClass("arrowUp");
						$("#detailsPanel").addClass("arrowDown");
						checkForPanelOverflow();
					}
				}
			);
		} else if($("#detailsContainer").html() != ""){
			$("#detailsHeader").append("<hr>");
			$("#detailsContainer").slideDown(
				{
					complete:function(){
						detailsOpen = true;
						$("#detailsPanel").removeClass("arrowDown");
						$("#detailsPanel").addClass("arrowUp");
						checkForPanelOverflow();
					}
				}
			);
		}
	});
	
	// Set click events on legends panel
	$("#legendsHeader").click(function() {
		if (legendsOpen){
			$("#legendsContainer").slideUp(
				{
					complete:function(){
						legendsOpen = false;
						$("#legendsContainer").html("");
						$("#legendsHeader").html("Legends");
						$("#legendsPanel").removeClass("arrowUp");
						$("#legendsPanel").addClass("arrowDown");
						checkForPanelOverflow();
					}
				}
			);
		} else {
			$("#legendsContainer").css('display', 'none');
			$("#legendsHeader").html("Legends<br /><hr />");
			$("#legendsContainer").html($("#legends").html());
			$("#legendsContainer").slideDown(
				{
					complete:function(){
						legendsOpen = true;
						$("#legendsPanel").removeClass("arrowDown");
						$("#legendsPanel").addClass("arrowUp");
						checkForPanelOverflow();
					}
				}
			);
		}
	});
	
	// Set click events on filtering panel
	$("#filteringHeader").click(function() {
		if (filteringOpen){
			$("#filteringContainer").slideUp(
				{
					complete:function(){
						filteringOpen = false;
						$("#filteringHeader").html("Filtering");
						$("#filteringPanel").removeClass("arrowUp");
						$("#filteringPanel").addClass("arrowDown");
						checkForPanelOverflow();
					}
				}
			);
		} else {
			$("#filteringContainer").css('display', 'none');
			$("#filteringContainer").html($("#filtering").html());
			$("#filteringHeader").html("Filtering<br /><hr />");
			$("#filteringContainer").slideDown(
				{
					complete:function(){
						filteringOpen = true;
						$("#filteringPanel").removeClass("arrowDown");
						$("#filteringPanel").addClass("arrowUp");
						checkForPanelOverflow();
						parseFilterQuery();
					}
				}
			);
		}
	});

	// Set click events on search panel
	$("#searchHeader").click(function() {
		if (searchOpen){
			$("#searchContainer").slideUp(
				{
					complete:function(){
						searchOpen = false;
						$("#searchHeader").html("Search");
						$("#searchPanel").removeClass("arrowUp");
						$("#searchPanel").addClass("arrowDown");
						checkForPanelOverflow();
					}
				}
			);
		} else {
			$("#searchContainer").css('display', 'none');
			$("#searchContainer").html($("#search").html());
			$("#searchHeader").html("Search<br /><hr />");
			$("#searchContainer").slideDown(
				{
					complete:function(){
						searchOpen = true;
						$("#searchPanel").removeClass("arrowDown");
						$("#searchPanel").addClass("arrowUp");
						checkForPanelOverflow();
						parseFilterQuery();
					}
				}
			);
		}
	});

	// Search when search field is changed
	setInterval("checkForSearch()", 200);
	
	// Close empty panels
	setInterval("closeEmptyPanels()", 1000);
	
}

var lastSearch = "";

function checkForSearch(){
	var val = $("#searchField").val();
	if (val != lastSearch){
		lastSearch = val;
		search(val);
	}
}

/**
 * Sets up the panels, event listeners and selection controller.
 */
function closeEmptyPanels(){
	if (detailsReadyToClose){
		$("#detailsContainer").slideUp(
			{
				complete:function(){
					$("#detailsHeader").html("Vessel details");
					detailsOpen = false;
					$("#detailsPanel").removeClass("arrowUp");
					$("#detailsPanel").addClass("arrowDown");
					checkForPanelOverflow();
					$("#detailsContainer").html("");
					detailsReadyToClose = false;

				}
			}
		);
	}
}

/**
 * Check if a panel overflows the window height.
 * The height of the panels will correct to fit.
 */
function checkForPanelOverflow(){
	var h = $(window).height();
	var lh = 370;				// The height used by legends
	var vdh = 496;				// The height of the vessel details
	var fih = 380;				// The height of the filtering
	var sh = 92;				// The height of the search bar

	if (searchOpen){
		sh = 92;
	} else {
		sh = 27;
	}
	h -= sh;
	
	if (legendsOpen && detailsOpen && filteringOpen){
		$("#detailsContainer").height(Math.min(vdh, (h-lh)/2));
		$("#detailsContainer").css("overflow-y", "scroll");
		$("#filteringContainer").height(Math.min(fih, (h-lh)/2));
		$("#filteringContainer").css("overflow-y", "scroll");
	} else if (legendsOpen && detailsOpen && !filteringOpen){
		$("#detailsContainer").height(Math.min(vdh, h-lh));
		$("#detailsContainer").css("overflow-y", "scroll");
	} else if (legendsOpen && !detailsOpen && filteringOpen){
		$("#filteringContainer").height(Math.min(fih, h-lh));
		$("#filteringContainer").css("overflow-y", "scroll");
	} else if (!legendsOpen && detailsOpen && filteringOpen){
		$("#detailsContainer").height(Math.min(vdh, h/2 - 70));
		$("#detailsContainer").css("overflow-y", "scroll");
		$("#filteringContainer").height(Math.min(fih, h/2 - 70));
		$("#filteringContainer").css("overflow-y", "scroll");
	} else if (!legendsOpen && !detailsOpen && filteringOpen){
		$("#filteringContainer").height(Math.min(fih, h - 60));
		$("#filteringContainer").css("overflow-y", "scroll");
	} else if (!legendsOpen && detailsOpen && !filteringOpen){
		$("#detailsContainer").height(Math.min(vdh, h - 110));
		$("#detailsContainer").css("overflow-y", "scroll");
	} else {
		$("#filteringContainer").height('');
		$("#filteringContainer").css("overflow-y", "auto");
		$("#detailsContainer").height('');
		$("#detailsContainer").css("overflow-y", "auto");
	}
}

/**
 * Updates the vessel details panel to show informaiton 
 * of a specific vessel.
 *
 * @param feature
 *            The feature of the vessel
 */
function updateVesselDetails(feature){
	// Get details from server
	$.getJSON(detailsUrl, {
			past_track: '1',
			id: feature.attributes.id
		}, function(result) {
			// Load and draw tracks
			var tracks = result.pastTrack.points;
			drawPastTrack(tracks);			

			// Load details
			$("#detailsContainer").html("");
			$("#vd_mmsi").html(result.mmsi);
			$("#vd_class").html(result.vesselClass);
			$("#vd_name").html(result.name);
			$("#vd_callsign").html(result.callsign);
			$("#vd_lat").html(result.lat);
			$("#vd_lon").html(result.lon);
			$("#vd_imo").html(result.imoNo);
			$("#vd_source").html(result.sourceType);
			$("#vd_type").html(result.vesselType);
			$("#vd_cargo").html(result.cargo);
			$("#vd_country").html(result.country);
			$("#vd_sog").html(result.sog + ' kn');
			$("#vd_cog").html(result.cog + ' &deg;');
			$("#vd_heading").html(result.heading + ' &deg;');
			$("#vd_draught").html(result.draught + ' m');
			$("#vd_rot").html(result.rot + ' &deg;/min');
			$("#vd_width").html(result.width + ' m');
			$("#vd_length").html(result.length + ' m');
			$("#vd_destination").html(result.destination);
			$("#vd_navStatus").html(result.navStatus);
			$("#vd_eta").html(result.eta);
			$("#vd_posAcc").html(result.posAcc);
			if (result.lastReceived != "undefined"){
				$("#vd_lastReport").html(	result.lastReceived.split(":")[0] + " min " + 
											result.lastReceived.split(":")[1] + " sec");
			} else {
				$("#vd_lastReport").html("undefined");
			}
			
			$("#vd_link").html('<a href="http://www.marinetraffic.com/ais/vesseldetails.aspx?mmsi=' + result.mmsi + '" target="_blank">Target info</a>');
			
			// Append details to vessel details panel
			$("#detailsContainer").append($("#vesselDetails").html());
			detailsReadyToClose = false;

			// Open vessel detals
			if (!detailsOpen){
				$("#detailsContainer").css('display', 'none');
				$("#detailsHeader").html("Vessel details<br /><hr />");
				$("#detailsContainer").slideDown(
					{
						complete:function(){
							detailsOpen = true;
							$("#detailsPanel").removeClass("arrowDown");
							$("#detailsPanel").addClass("arrowUp");
							checkForPanelOverflow();
							$("#detailsPanel").css('background-image', 'url("../img/arrowUp.png"');
						}
					}
				);
			}
		});
}

