
'''
Yixin Chen
Mar 24, 2019
Predict Crime Count for LA county

'''

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

#read city boundries of LA county, and dissolve by city name
lacountry = gpd.read_file('.\\City Boundaries\\LACountyBoundaries.shp')
lacountry = lacountry.dissolve(by='city_name')
lacountry.head()

#clean wrong city labels
lacountry.at['Industry', 'city_label'] = 'Industry'
lacountry.at['La Canada Flintridge', 'city_label'] = 'La Canada Flintridge'
lacountry.at['Unincorporated', 'city_label'] = 'Unincorporated'

# create tract centroid point shapes, for joining with census tract shapefile
#read census tracts shapefile, read crimePredict data and merge them on GEOID10
tracts = gpd.read_file('.\\CENSUS_TRACTS_2010\\CENSUS_TRACTS_2010.shp')
crimePredict = pd.read_csv('.\\crime_data\\predictCrimeLACounty.csv', dtype={'GEOID10': str})

tracts = tracts.merge(crimePredict, on = 'GEOID10')
tracts.head()

# create tract centroid point shapes, for joining with census tract shapefile
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

#spatial join cities boundries and census tract points
laCounty = gpd.sjoin(tract_centroids, lacountry, how="inner", op='within')
laCounty.head()

#merge data
laCounty = tracts.merge(laCounty, on = 'GEOID10')
laCounty.head()

#write to shapefile
laCounty=gpd.GeoDataFrame(laCounty, crs=tracts.crs, geometry=laCounty.geometry_x)
temp = laCounty.loc[:, ['GEOID10', 'city_label', 'Burglar', 'geometry']]
temp.head()
temp.to_file(".\\LA County Predit\\laCounty\\" + "laCounty" +".shp")

#creat json file
import shapefile
from json import dumps

# read the shapefile
reader = shapefile.Reader(".\\LA County Predit\\laCounty\\" + "laCounty" +".shp")
fields = reader.fields[1:]
field_names = [field[0] for field in fields]
buffer = []
for sr in reader.shapeRecords():
    atr = dict(zip(field_names, sr.record))
    geom = sr.shape.__geo_interface__
    buffer.append(dict(type="Feature", geometry=geom, properties=atr)) 
geojson = open(".\\LA County Predit\\laCounty\\" + "laCounty" +".json", "w")
geojson.write(dumps({"type": "FeatureCollection", "features": buffer}, indent=2) + "\n")
geojson.close()

#creat js variable for crime mapping
alldata ='var laCountyCrimePredit ='
f = open(".\\LA County Predit\\laCounty\\" + "laCounty" +".json",'r')
filedata = f.read()
f.close()

newdata = filedata.replace(" " ,"")
newdata = newdata.replace("\n" ,"")
newdata = newdata.replace("}},", "}},\n")
alldata = alldata + newdata + ";\n\n"

f = open(".\\LA County Predit\\laCounty\\laCountyCrimePredit.js",'w')
f.write(alldata)
f.close()

