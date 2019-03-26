/*
3/17/2019
crimeMap.js
Version 3.14
get crime data from database based on user selections, and draw a interactive map accordingly

*/

$(document).ready(function(){
		
/* 	var lat, lon;
	var crimeMap;
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	}
	
	function showPosition(position) {
		lat = position.coords.latitude;
		lon = position.coords.longitude;
		console.log(lat);
		console.log(lon);
		crimeMap = L.map('crimeMap',{peferCanvas: true}).setView(lon, lat, 11);
	} */

	//default city
	var currentCity = "Los Angeles";
	//default map view
	var crimeMap = L.map('crimeMap',{peferCanvas: true}).setView([cityCenter[currentCity][1], cityCenter[currentCity][0]], 11);
	
	//base map and attribution, openstreetmap
	L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: 'Map data &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'	
	}).addTo(crimeMap);
	
	/**************************/
	//map controls
	//upright corner info
	var info = L.control();	
	//legend
	var legend = L.control({position: 'bottomleft'});
	var geojson;

	//div of info
	info.onAdd = function (crimeMap) {
		this._div = L.DomUtil.create('div', 'info');
		this.update();
		return this._div;
	};

	//mouse hover event, highlight county and show the value in upright corner
	function highlightFeature(e) {
		var layer = e.target;
		layer.setStyle({
			weight: 2,
			color: '#666',
			dashArray: '',
			opacity: 1,
			fillOpacity: 0.7
		});
		
		//these browsers do not support bringToFront()
		if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {			
			layer.bringToFront();
		}
		//info layer update
		info.update(e, layer.feature.properties);
	}

	//reset highlight when mouse out
	function resetHighlight(e) {
		geojson.resetStyle(e.target);
		info.update();
	}

	//click to zoom to feature
	function clickFeature(e){
		crimeMap.fitBounds(e.target.getBounds());
	}

	//add highlight and zoom event to map
	function onEachFeature(feature, layer) {
		if (feature.properties) {
			layer.bindPopup((currentCity == "Los Angeles County" ? feature.properties.city_label + '\n' : '') +feature.properties.GEOID10 + ": " + parseInt(feature.properties.Burglar));
		}
		layer.on({
			mouseover: highlightFeature,
			mouseout: resetHighlight,
			click: clickFeature
		});
	}
	/**************************/

//real los angeles data
/*	
	for(var i = 0; i < laTract.features.length; i++){
		laTract.features[i].properties.value.push(laCrimeCount[i]);
	}
*/	
	getMapData(currentCity);

	
	//on city selection change
	$("#locSelect").change(function(){
		//selected location
		currentCity = $("#locSelect").children(":selected").attr("id");
		
		//remove previous layers
		crimeMap.eachLayer(function (layer) {
			crimeMap.removeLayer(layer);
		});
		
		//set view to center of current city
		crimeMap.setView([cityCenter[currentCity][1], cityCenter[currentCity][0]], currentCity == "Los Angeles" ? 11 : 
																					currentCity == "Unincorporated" ? 10 : 
																					currentCity == "Los Angeles County" ? 10 : 13);
		
		//reload base map and attribution, openstreetmap
		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			maxZoom: 19,
			attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'	
		}).addTo(crimeMap);
		
		//add layer of current city to map
		if(currentCity != "Los Angeles County") L.geoJson(laCountyCrimePredit[currentCity]).addTo(crimeMap);
		else L.geoJson(laCountyPredit).addTo(crimeMap);
		
		//load map data
		getMapData(currentCity);
    });
	
	function getMapData(location){
		//data of selected city
		if(currentCity != "Los Angeles County") 
			var currentData = laCountyCrimePredit[location];
		else 
			var currentData = laCountyPredit;
		//extract and sort predicted crime count of selected city
		var crimeCount = [];
		for(var i = 0; i < currentData.features.length; i++){
			crimeCount.push(currentData.features[i].properties.Burglar);
		}
		crimeCount.sort(numberSort);
		
		//get color grade based on sorted data, 0-12.5%, 12.5-25%, 25-37.5%, 37.5-50%, 50-62.5%, 62.5-75%, 75-87.5% and above 80%
		var grade = [];
		grade.push(0);
		
		//real los angeles data
/* 		for(var i = 0.125; i < 1; i += 0.125){
			grade.push(crimeCount[parseInt(crimeCount.length * i) - 1]);
		} */
		
		if(crimeCount.length >= 7){
			for(var i = 0.125; i < 1; i += 0.125){
				grade.push(crimeCount[parseInt(crimeCount.length * i) - 1]);
			}
		}
		else{
			for(var i = 0; i < crimeCount.length; i++){
				grade.push(crimeCount[i]);
			}
		}
		
		
		//set map style: color, border, opacity
		function style(feature) {
			return {
				fillColor: getColor(feature.properties.Burglar, grade),
				weight: 0.5,
				opacity: 1,
				color: 'white',
				dashArray: '3',
				fillOpacity: 0.6
			};
		}
		
		//add style to map
		L.geoJson(currentData, {style: style}).addTo(crimeMap);
		
		var labelText = currentCity;
		
		//update info box
		info.update = function (e, props) {
			this._div.innerHTML = '<h4>'+labelText+'</h4>' +  
			
			//'<b style="color:orange;">' + currentCity + '</b><br />' +
			(props ? (currentCity == "Los Angeles County" ? '<b style="color:orange;">' + "City of " + props.city_label + '</b><br />' : 
									'') + '<b style="color:#60A5E7;">' + 'Census Tract ID: ' + props.GEOID10 + '<br>' + 'Crime Count: ' + parseInt(props.Burglar) + '</b>' : 
									'Hover over a Census Tract');
		};
		info.addTo(crimeMap);
		
		//add info to map
		geojson = L.geoJson(currentData, {
			style: style,
			onEachFeature: onEachFeature
		}).addTo(crimeMap);
		
/* 		//change border color upon selection     
		geojson.eachLayer(function (layer) {
  			if(layer.feature.properties.COUNTY == currentCounty) {
    			layer.setStyle({color: '#F5892E', weight: 2}); 
				layer.bringToFront();
  			}
			else layer.setStyle({color: 'white', weight: 1}); 
		});
		 */
		 
		//add legend based on data			
		legend.onAdd = function (crimeMap) {
			var div = L.DomUtil.create('div', 'info legend'),
				grades = grade,
				labels = [],
				from, to;

			for (var i = 0; i < grades.length; i++) {
				from = parseInt(grades[i]);
				to = parseInt(grades[i + 1]);
				labels.push(
					'<i style="background:' + getColor(from + 1, grade) + '"></i> ' +
					from + (to ? '&ndash;' + to : '+'));
			}

			div.innerHTML = labels.join('<br>');
			return div;
		};
		legend.addTo(crimeMap);
		
	}
	
	//color grade
	function getColor(d, colorGrade) {
		for(var i = colorGrade.length - 1; i >= 0; i--){
			if(d > colorGrade[i]) return color[i];
		}
	} 
	

	//sorting by numeric value
	var numberSort = function (a,b) {
		return a - b;
	};

});

var color = [];
color.push('#FFEDA0');
color.push('#FED976');
color.push('#FEB24C');
color.push('#FD8D3C');
color.push('#FC4E2A');
color.push('#E31A1C');
color.push('#BD0026');
color.push('#800026');


