
'''
Yixin Chen
Mar 24, 2019
Predict crime count for each city of LA county


'''

#predict crime for each city in LA county

#libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
from shapely.geometry import Point
import geopandas as gpd
import pysal as ps

# showing plots
get_ipython().run_line_magic('matplotlib', 'inline')

# showing more rows and columns when outputing table
pd.set_option('display.max_columns', 500)
pd.set_option('display.max_rows', 500)

#showing multiple outputs per cell
from IPython.core.interactiveshell import InteractiveShell
InteractiveShell.ast_node_interactivity = "all"

#city boundaries in LA county
lacountry = gpd.read_file('.\\City Boundaries\\LACountyBoundaries.shp')
lacountry
lacountry.plot()


#each city has several lines of record, dissolve them into one record
lacountry = lacountry.dissolve(by='city_name')
lacountry
lacountry.plot()

#extract city name as a list, and correct city label
citylist = lacountry.city_label.tolist()

for i in range(len(citylist)):
    if(citylist[i] == 'City of Industry'):
        citylist[i] = 'Industry'
    if(citylist[i] == 'La Ca?ada Flintridge'):
        citylist[i] = 'La Canada Flintridge'
    if(citylist[i] == 'CO'):
        citylist[i] = 'Unincorporated'

	
#creat seperate GeoDataFram for each city
cityGeoList={}
for city in citylist:
    cityGeoList[city] = lacountry.loc[[city], :]


# create tract centroid point shapes, for joining with census tract shapefile
tracts = gpd.read_file('.\\CENSUS_TRACTS_2010\\CENSUS_TRACTS_2010.shp')

# read predicted data and change data type of GEOID10 to String, for merging
crimePredict = pd.read_csv('.\\crime_data\\predictCrimeLACounty.csv', dtype={'GEOID10': str})

#merge census tract shapefile and predicted data
tracts = tracts.merge(crimePredict, on = 'GEOID10')
tracts.head()

#create point shape for each census tract, for spatial joining with city boundries
geometry = [Point(xy) for xy in zip(tracts.X_Center, tracts.Y_Center)]
df = tracts['GEOID10']
tract_centroids = gpd.GeoDataFrame(df, crs=tracts.crs, geometry=geometry)
tract_centroids.head()

# check crs
lacountry.crs
tracts.crs
tract_centroids.crs

# unify crs
tract_centroids = tract_centroids.to_crs(lacountry.crs)
tract_centroids.crs
tracts = tracts.to_crs(lacountry.crs)
tracts.crs

#spatial join census data with city boundries geodata
for city in citylist:
    cityGeoList[city] = gpd.sjoin(tract_centroids, cityGeoList[city], how="inner", op='within')

for city in citylist:
    cityGeoList[city] = tracts.merge(cityGeoList[city], on = 'GEOID10')

#write data of each cities to shape file
for city in citylist:
    cityGeoList[city]=gpd.GeoDataFrame(cityGeoList[city], crs=tracts.crs, geometry=cityGeoList[city].geometry_x)
    temp =  cityGeoList[city].loc[:, ['GEOID10', 'Burglar', 'geometry']]
    temp.head()
    temp.to_file(".\\LA County Predit\\" + city +".shp")

#check data
cityGeoList['Los Angeles'].head()

#write predicted crime data of each cities to geoJSON files for using in the webApp
#creat seperate var for each city
import shapefile
from json import dumps

# read the shapefiles, and write to JSON files
for city in citylist:
    reader = shapefile.Reader(".\\LA County Predit\\" + city +".shp")
    fields = reader.fields[1:]
    field_names = [field[0] for field in fields]
    buffer = []
    for sr in reader.shapeRecords():
        atr = dict(zip(field_names, sr.record))
        geom = sr.shape.__geo_interface__
        buffer.append(dict(type="Feature", geometry=geom, properties=atr)) 
    geojson = open(".\\LA County Predit\\geoJSON\\" + city +".json", "w")
    geojson.write(dumps({"type": "FeatureCollection", "features": buffer}, indent=2) + "\n")
    geojson.close()

#write to js file, which can be used directly by the webApp
alldata ='var laCountyCrimePredit = []; \n\n'
for city in citylist:
    f = open(".\\LA County Predit\\geoJSON\\" + city +".json",'r')
    filedata = f.read()
    f.close()

    newdata = filedata.replace(" " ,"")
    newdata = newdata.replace("\n" ,"")
    newdata = newdata.replace("}},", "}},\n")
    newdata = 'laCountyCrimePredit["' + city + '"]=' + newdata
    alldata = alldata + newdata + ";\n\n"

f = open(".\\LA County Predit\\geoJSON\\laCountyCrimePredit.js",'w')
f.write(alldata)
f.close()


#get city center, for online map display, zoom to the selected city
citycenter = lacountry.loc[:,['geometry']]
citycenter.head()

citycenter['center'] = citycenter['geometry'].centroid

mylistx = citycenter['center'].apply(lambda p: p.x).tolist()
mylistx
mylisty = citycenter['center'].apply(lambda p: p.y).tolist()
mylisty

allcenters ='var cityCenter = []; \n\n'
for i in range(len(mylistx)):
    newdata ='['
    newdata = newdata + str(mylistx[i]) + "," + str(mylisty[i]) + "]"
    newdata = 'cityCenter["' + citylist[i] + '"]=' + newdata
    allcenters = allcenters + newdata + ";\n\n"

f = open(".\\LA County Predit\\geoJSON\\cityCenter.js",'w')
f.write(allcenters)
f.close()

