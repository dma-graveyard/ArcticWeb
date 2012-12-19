var my_ship;

$(document).ready(function() {

	// Search after own ship based on ship_mmsi
	$.getJSON(searchUrl, { argument: ship_mmsi }, function (result) {
			var s = "s";
			
			// Show search results
			$("#searchResults").css('visibility', 'visible');
				
			// Search results
			searchResults = [];

			// Get vessels
			for (vesselId in result.vessels) {
				var vesselJSON = result.vessels[vesselId];
				var vessel = new Vessel(vesselId, vesselJSON, 1);
				searchResults.push(vessel);
			}

			// Add search result to list
			if (searchResults.length <= searchResultsLimit && searchResults.length != 0){
				if (searchResults.length == 1){
					my_ship = searchResults[0];
				}
				
			}

		});
	
});

function goToMyShip(){
	
	var myShipSearch;
	
	$.each(vessels, function(key, value) { 

		if (value.mmsi == ship_mmsi){
			
			myShipSearch = value;
			
		}
		
	});
	
	markedVessel = my_ship;
	
	goToVessel(my_ship);
	
}