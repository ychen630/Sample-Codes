
'''
Yixin Chen
Mar 24, 2019
Geographic data processing

'''


# ## Los Angeles City Census Tracts
# ### 11-25-2018

# import libraries
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

#check current folder
#os.getcwd()
#if not root folder, change directory
#os.chdir('PATH')


# ### spatial join LA city boundary with LA county tracts shape file
# to find out the list of tracts fall within the LA city boundary

# read in LA city boundary
city = gpd.read_file('.\\City_Boundary\\City_Boundary.shp')

# create tract centroid point shapes
tracts = gpd.read_file('.\\CENSUS_TRACTS_2010\\CENSUS_TRACTS_2010.shp')
geometry = [Point(xy) for xy in zip(tracts.X_Center, tracts.Y_Center)]
df = tracts['GEOID10']
tract_centroids = gpd.GeoDataFrame(df, crs=tracts.crs, geometry=geometry)
tract_centroids.plot()

# check crs
city.crs
tracts.crs
tract_centroids.crs

# unify crs
tract_centroids = tract_centroids.to_crs(city.crs)
tract_centroids.crs

# plot LA city shape
city.plot()

# spatial join to get list of tracts fall within LA city boundary
la_tracts = gpd.sjoin(tract_centroids, city, how="inner", op='within')

# number of rows before and after sjoin
tract_centroids.shape[0]
la_tracts.shape[0]

# plot la city tracts centroids
la_tracts.plot()

# attribute join with tract shapes
tracts = tracts.to_crs(city.crs)
tracts_in_lacity =tracts.merge(la_tracts, on = 'GEOID10')

# plot tracts in la city
tracts_in_lacity=gpd.GeoDataFrame(tracts_in_lacity, crs=tracts.crs, geometry=tracts_in_lacity.geometry_x)
tracts_in_lacity.plot()
# tracts_in_lacity.to_csv(".\\tracts_in_lacity.csv")

# drop geo features
tracts_in_lacity.drop(list(tracts_in_lacity.filter(regex = 'geometry_')), axis = 1, inplace = True)

#write to file
tracts_in_lacity.to_file(".\\tracts_inla\\tracts_in_lacity.shp")

# gpd to_file does not save crs, this is left as is, not addressed, thus in future steps needs manual specification of crs to
# {'init': 'epsg:4326'}
# https://gis.stackexchange.com/questions/204201/geopandas-to-file-saves-geodataframe-without-coordinate-system

tracts_in_lacity['GEOID10'].describe()
tracts_in_lacity.crs

