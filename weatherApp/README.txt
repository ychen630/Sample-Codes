2017 Spring
COMP 484
Final Project - Weather App using OpenWeatherMap API
Yixin Chen

1)	This is the final project for the CSUN COMP 484 course in 2017 Spring Semester. This weather app is a web-based application that allows users to check the current weather information and 5 days forecast of any city around the world. The application asks users to select a country and then select a city in that country, and after submitting, the application will list the current and forecast weather information. This weather app uses the openweathermap API which is found at www.openweathermap.org

2)	This weather app has three features (button are listed in the up right corner). 

1. Current weather information. Users first select a country, then select a city in that country, and at last click the “Show Weather” button, the app will list the current weather information for the selected city. The weather information includes: weather type, weather description (includes a related weather icon), temperature, pressure, humidity, min temperature, max temperature, wind speed and wind direction. All units are metric. 

2. 5 day weather forecast. This feature carries the city selected in the current weather page as default city. If no city is selected by users, when clicking the “show forecast” button, the app will list the weather forecast of the default city. If users select a city in the forecast page, the app will list weather forecast of the selected city. After users click the “show forecast”, a “show temperature chart” button will be displayed. If users click this button, a line chart of the temperature for the next 5 days will be displayed in a new page. 

3. Contact information. This page allows users to contact me. Users need to fill in the form (the app will validate the input automatically) first, and then click the “send” button, the app will send me an email containing the information users have input.

Note: The openweathermap api use "http" but not "https". If the website is opened by Google Chrome, the Javascript may not be automatically loaded, and an error will be displayed at the end of the address bar: "This page is trying to load scripts from unauthenticated sources".  To fix this error, you can choose to override the alert for the page by clicking Load unsafe script. Chrome will refresh the page and load its content. Firefox may not have this error. 