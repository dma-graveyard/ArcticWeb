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
			
			markedVessel = my_ship;

		});
});

function goToMyShip(){
	
	markedVessel = my_ship;
	
	goToVessel(my_ship);
	
}

function loadSelectedInformation(){
	
	if (!selectedVessel){
		return;
	}
	
	searchedVessel;
	
	// Search after own ship based on ship_mmsi
	$.getJSON(searchUrl, { argument: selectedVessel.mmsi }, function (result) {
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
					searchedVessel = searchResults[0];
				}
				
			}
			
			insertSelectedInformation(searchedVessel);

		});
	
}

function insertSelectedInformation(vessel){
	
	alert(vessel);
	
}
