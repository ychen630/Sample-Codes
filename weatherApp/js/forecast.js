//get 5 days weather forcast from OpenWeatherMap API
//display results on page


//Passing variables from index.html using localStorage
//this is the default country and city from the current weather information page
//if the user wants to see the weather forcast of the city of the current weather page
//do not need to input the city name, just click the show forcast button
var currentcity=localStorage.getItem("selectCity");
var currentcountry=localStorage.getItem("selectCountry");

$(document).ready(function(){
	$("#submitForecast").click(function(){
		return getForecast();
	});
});

function getForecast(){
	//get city and country input
	var city = $("#Cities").val();
	var country = $("#Countries").val();
	console.log($("#Cities").val());
	
	//if no city input, show weather information of the city input from the current weather page
	if(city == 0) {
		city = currentcity;
		country = currentcountry;
	}
	//call API and display results
	if(city != 0 && city !=''){
		$.ajax({
			url:'https://api.openweathermap.org/data/2.5/forecast?id=' + city + "&units=metric" + "&APPID=9e860fd6d4790893adbc5e5715cc3045",
			type:"GET",
			dataType:"jsonp",
			success:function(data){
				//table header
				var table = "<tr><th>Time</th><th>Icon</th><th>Weather Type</th><th>Weather Detail</th><th>Temperature</th><th>Pressure</th><th>Humidity</th><th>Wind Speed</th><th>Wind Direction</th></tr>";				
				var header = '<h3 class="text-primary" style="margin-top:20px;">Weather forecast for '+ data.city.name + ', ' +country+'</h3>';
				var count = 0;
				//loop the result and get weather information for five days
				for(var i = 0; i < data.list.length; i++){
					var time = data.list[i].dt_txt;
					var date = time.substring(0, 10);
					var hour = time.substring(11,13);
					if(hour == 12){
						//build table with results
						count++;
						table += "<tr>";
						table += "<td align='center'>" + date+"</td>";
						table += "<td align='center'><img src='https://openweathermap.org/img/w/"+data.list[i].weather[0].icon+".png'></td>";
						table += "<td align='center'>" + data.list[i].weather[0].main+"</td>";
						table += "<td align='center'>" + data.list[i].weather[0].description+"</td>";
						table += "<td align='center'>" + Math.round(data.list[i].main.temp)+"&deg;C</td>";
						table += "<td align='center'>" + data.list[i].main.pressure+" hpa</td>";
						table += "<td align='center'>" + data.list[i].main.humidity+"%</td>";
						table += "<td align='center'>" + data.list[i].wind.speed+" m/s</td>";
						table += "<td align='center'>" + Math.round(data.list[i].wind.deg)+"&deg;</td>";						
						table+="</tr>";
//						console.log(count);
						$("#input" + count).val(Math.round(data.list[i].main.temp));
						
						/*
						if(count ==1)
							localStorage.setItem("temp1",data.list[i].main.temp);
						if(count ==2)
							localStorage.setItem("temp2",data.list[i].main.temp);
						if(count ==3)
							localStorage.setItem("temp3",data.list[i].main.temp);
						if(count ==4)
							localStorage.setItem("temp4",data.list[i].main.temp);
						if(count ==5)
							localStorage.setItem("temp5",data.list[i].main.temp);
						*/				
					}
				}
				
				
				//display result
				$("#forecastWeather").html(table);			
				$("#header").html(header);
			
				//fill city and country to form and then send to showchart.php
				$("#input6").val(data.city.name);
				$("#input7").val(country);
//				localStorage.setItem("city",data.city.name);
//				localStorage.setItem("country",country);

				//reset city and country
				$("#Cities").val('');
				$("#Countries").val('');
				
				
			}
		});
	}else{
		//no city input in this page and the current page, error
		$("#error").html("<div class='alert alert-danger' id='errorCity'>Please Enter City Name</div>");
	}
}

//click to show hidden "show chart" botton
function unhide(clickedButton, divID) {
	var item = document.getElementById(divID);
	if (item) {
		if(item.className=='hidden'){
			item.className = 'unhidden' ;
		}else{
			item.className = 'hidden';
			clickedButton.value = 'unhide'
		}
	}
}

	