//get current weather information using OpenWeatherMap API and display
//results on page

$(document).ready(function(){
	$("#submitCity").click(function(){
		return getWeather();
	});
});

//get current weather from openweathermap API
function getWeather(){
	//get country and city input from user
	var city = $("#Cities").val();
	var country = $("#Countries").val();
	
	//store to localStorage for passing to another page
	localStorage.setItem("selectCity",city);
	localStorage.setItem("selectCountry",country);

	//call API using the city input
	if(city != 0){
		$.ajax({
			url:'http://api.openweathermap.org/data/2.5/weather?id=' + city + "&units=metric" + "&APPID=9e860fd6d4790893adbc5e5715cc3045",
			type:"GET",
			dataType:"jsonp",
			success:function(data){
				var widget = showResults(data)			
				$("#showWeather").html(widget);
				//reset city and country to NULL
				$("#Cities").val('');
				$("#Countries").val('');
			}
		});
	}else{
		//no city input, error
		$("#error").html("<div class='alert alert-danger' id='errorCity'>Please Enter City Name</div>");
	}
}

//show current weather information
function showResults(data){
	return '<h3 class="text-primary" style="margin-top:20px;">Current Weather for '+data.name+', '+$("#Countries").val()+'</h2>'+
			"<h3><strong>Weather</strong>: "+data.weather[0].main+"</h3>"+
			"<h3><strong>Description</strong>: <img src='http://openweathermap.org/img/w/"+data.weather[0].icon+".png'>"+data.weather[0].description+"</h3>"+
			"<h3><strong>Temperature</strong>: "+data.main.temp+" &deg;C</h3>"+
			"<h3><strong>Pressure</strong>: "+data.main.pressure+" hpa</h3>"+
			"<h3><strong>Humidity</strong>: "+data.main.humidity+"%</h3>"+
			"<h3><strong>Min Temperature</strong>: "+data.main.temp_min+"&deg;C</h3>"+
			"<h3><strong>Max Temperature</strong>: "+data.main.temp_max+"&deg;C</h3>"+
			"<h3><strong>Wind Speed</strong>: "+data.wind.speed+"m/s</h3>"+
			"<h3 padding-bottom:30px;'><strong>Wind Direction</strong>: "+data.wind.deg+"&deg;</h3>";
}