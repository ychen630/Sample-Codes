$(document).ready(function(){
	//year list
	var listCities= "";
	for (var i = 0; i < cityList.length; i++){
		listCities+= "<option id='" + cityList[i] + "' value= '" + cityList[i] + "'>" + cityList[i] + "</option>";
	}	
	$("#locSelect").html(listCities);
});