
'''
Yixin Chen
Mar 24, 2019
Crime data preprocessing

'''

# import libraries
import pandas as pd
import numpy as np
import geopandas as gpd
from shapely.geometry import Point
import os
import matplotlib.pyplot as plt

# check folder, change if needed
#os.getcwd()
# os.chdir("PATH")


# Crime Data Processing

#load raw data
df = pd.read_csv('.\\crime_data\\Crime_Data_from_2010_to_Present.csv')
#extract useful column
df1=df.loc[:,['DR Number','Date Occurred','Crime Code','Crime Code Description','Location ']]

# Parsing location Latitude and Longitude
# Note, the column name for location has an extra space at the end
location = df['Location '].str[1:-1].str.split(',', expand=True).astype(float)
location.columns = ['Latitude', 'Longitude']

# add back to main data, 
df2 = pd.concat([df1, location], axis=1)

# create numeric year variable
df2['Year']=df2['Date Occurred'].str[-4:]
pd.to_numeric(df2['Year'], errors='coerce')

# identify all crime codes with 'BURGLARY' in description, and create a new Buglary feature (0/1)
df2['Burglary'] = df2['Crime Code Description'].str.contains('BURGLARY')
# check frequency of new feature
df2['Burglary'].value_counts()

# drop outlier values
dfb = df2.drop(df2[(df2.Latitude == 0)|(df2.Longitude == 0)].index)
dfb.plot.scatter(x='Longitude', y='Latitude')
# check frequency of new feature
dfb['Burglary'].value_counts()

# check if there's missing values in latitude and longitude
dfb.Latitude.isnull().sum()
dfb.Longitude.isnull().sum()
dfb.dropna(subset=['Latitude','Longitude'], inplace=True)
# check frequency of new feature
dfb['Burglary'].value_counts()

# create point shape from coordinates
geometry = [Point(xy) for xy in zip(dfb.Longitude, dfb.Latitude)]

# append back to data
crs = {'init': 'epsg:4326'}
gdf = gpd.GeoDataFrame(dfb, crs=crs, geometry=geometry)

#### @@geographic data processing done in separate file@@###
#### @@landuse data processing done in separate file@@###

# tracts as polygon shapes
tracts = gpd.read_file('.\\landuseP\\Tracts in LA with Landuse\\tracts_inla_with_landuse.shp')

# drop unused columns
tracts=tracts.loc[:,['GEOID10','Single_Fam','Multi_Fami','Commercial','Open_Space','Industrial','Parking','geometry']]

# Need to make sure the sources have the same crs.
# Check crs of the tract GeoDataFrame
tracts.crs
# Check crs of the crime GeoDataFrame
gdf.crs

# Spatial Join crime with tract
gdf_trt = gpd.sjoin(gdf, tracts, how="left")

# subset to burglary
burglary =gdf_trt[gdf_trt['Burglary']==True]

# summarize counts at tract level
#burglary_tr=burglary.TRACTID.value_counts().reset_index().rename(columns={'index': 'TRACTID', 0: 'Burglary Occurance'})
burglary_tr=pd.DataFrame(burglary.groupby(['GEOID10', 'Year'])['Burglary'].value_counts().fillna(0))
burglary_tr=burglary_tr.rename(columns={'Burglary': 'Burglary Occurrence'})
burglary_tr.reset_index(inplace=True)
burglary_tr=burglary_tr.drop(columns=['Burglary'])

# reshape long to wide
bg_tr = burglary_tr.pivot(index='GEOID10', columns='Year', values='Burglary Occurrence').fillna(0)
bg_tr.columns = ['BGLRY10', 'BGLRY11','BGLRY12','BGLRY13','BGLRY14','BGLRY15','BGLRY16','BGLRY17','BGLRY18']
bg_tr.reset_index(inplace=True)

# create files for each year
######use 2017 data######
bglry17 = bg_tr[['GEOID10','BGLRY17']]
#bglry17['GEOID10']=bglry17['GEOID10'].astype(str)

######2010######
#bglry10 = bg_tr[['GEOID10','BGLRY10']]
#bglry10['GEOID10']=bglry10['GEOID10'].astype(str)

######2011######
#bglry11 = bg_tr[['GEOID10','BGLRY11']]
#bglry11['GEOID10']=bglry11['GEOID10'].astype(str)

######2012######
#bglry12 = bg_tr[['GEOID10','BGLRY12']]
#bglry12['GEOID10']=bglry12['GEOID10'].astype(str)

######2013######
#bglry13 = bg_tr[['GEOID10','BGLRY13']]
#bglry13['GEOID10']=bglry13['GEOID10'].astype(str)

######2014######
#bglry14 = bg_tr[['GEOID10','BGLRY14']]
#bglry14['GEOID10']=bglry14['GEOID10'].astype(str)

######2015######
#bglry15 = bg_tr[['GEOID10','BGLRY15']]
#bglry15['GEOID10']=bglry15['GEOID10'].astype(str)

######2016######
#bglry16 = bg_tr[['GEOID10','BGLRY16']]
#bglry16['GEOID10']=bglry16['GEOID10'].astype(str)

######2018######
#bglry18 = bg_tr[['GEOID10','BGLRY18']]
#bglry18['GEOID10']=bglry18['GEOID10'].astype(str)

#attribute join of crime data to landuse data
bglry17 = pd.merge(bglry17, tracts, on='GEOID10')

# read acs data
acs16=pd.read_csv('.\\acs_tract\\R11645621_SL140.csv')

# create acs variables
# race diversity index
race = ['PCT_SE_T013_002',
        'PCT_SE_T013_003',
        'PCT_SE_T013_004',
        'PCT_SE_T013_005',
        'PCT_SE_T013_006',
        'PCT_SE_T013_007',
        'PCT_SE_T013_008']

def div_index(X):
    div = lambda x: x/100
    a=X.loc[:,race].apply(div)
    X['RaceDivIndx']=1-(a.apply(np.square).sum(axis=1))
    X['GEOID10']=acs16['Geo_GEOID'].str[-11:]
    
div_index(acs16)

# neighborhood disadvantage index
Nbrhood = ['PCT_SE_T025_002',
           'PCT_SE_T033_006',
           'PCT_SE_T118_002']
scale = acs16.loc[:, Nbrhood].describe()
scale.loc['mean','PCT_SE_T025_002']
scale.loc['std','PCT_SE_T025_002']

def nhood_idx(X, lst):
    a = pd.DataFrame()
    for var in lst:       
        a[var] = (X.loc[:, var]-scale.loc['mean', var])/scale.loc['std', var]
    X['NhoodDisIdx'] = a.mean(axis=1)
nhood_idx(acs16, Nbrhood)


#attribute join of crime data to acs data by tractid and check the new data
d17 = pd.merge(bglry17, acs16, on='GEOID10')

d17=d17.fillna(0)
# analysis data file
d17.to_csv('.\\crime_data\\bglry17_with_landuse.csv')

#no longer need geo features
d17 = d17.drop(columns=['geometry'])
#write to file
d17.to_csv('.\\crime_data\\bglry17_with_landuse_noGeometry.csv')
