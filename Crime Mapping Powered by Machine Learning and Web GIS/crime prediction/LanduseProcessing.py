
'''
Yixin Chen
Mar 24, 2019
Process land use data

'''

get_ipython().run_line_magic('matplotlib', 'inline')
import pandas as pd
import geopandas as gpd
import matplotlib.pyplot as plt
from shapely.geometry import Point
import os

import fiona
from shapely.geometry import shape

# showing multiple outputs per cell
from IPython.core.interactiveshell import InteractiveShell
InteractiveShell.ast_node_interactivity = "all"

# check and set current working directory
#os.getcwd()
#os.chdir('PATH')

# read landuse los angeles city shapefile
land = gpd.read_file('.\\landuse\\landuse.shp')
#read census tracts shape file
tracts_inla = gpd.read_file(".\\tracts_inla\\tracts_in_lacity.shp")

# check crs
tracts_inla.crs 
land.crs

# assign and convert crs
tracts_inla.crs = {'init': 'epsg:4326'}


land = land.to_crs(tracts_inla.crs)
land.crs
tracts_inla.crs 

#extract Multi-Family Residential type  mfr
#key words: Medium, High
mfr = land[(land["GPLU_DESC"].str.contains("Medium|High")==True)]
#exclude keyword: Highway
mfr = mfr[(mfr["GPLU_DESC"].str.contains("Highway") == False)]

#merge different types into 'Multi-Family Residential'
#mfr = mfr.assign(GPLU_DESC = 'Multi-Family Residential')

#extract Single-Family Residential type  sfr
#key words: Minimum, Low
sfr = land[(land["GPLU_DESC"].str.contains("Minimum|Low")==True)]
#exclude keyword: Medium
sfr = sfr[(sfr["GPLU_DESC"].str.contains("Medium") == False)]

#extract Commercial type  commercial
#key words: Commercial, Highway
commercial = land[(land["GPLU_DESC"].str.contains("Commercial|Highway")==True)]
#exclude keyword: Industrial
commercial = commercial[(commercial["GPLU_DESC"].str.contains("Industrial|Manufacturing") == False)]

#extract Open Space and Public Facilities type   ospf
#key words: Open, Public
ospf = land[(land["GPLU_DESC"].str.contains("Open|Public")==True)]

#extract Industrial type   indus
#key words: Manufacturing, Industrial
indus = land[(land["GPLU_DESC"].str.contains("Manufacturing|Industrial")==True)]

#extract Parking type   parking
#key words: Parking
parking = land[(land["GPLU_DESC"].str.contains("Parking")==True)]

#join landuse with tract 
#for now, just get the yes/no flagb
#mfr_tract = gpd.sjoin(tracts_inla, mfr, how="inner", op='intersects')
#mfr_tract.head()

#write to new shp file, for calculating landuse percentage
#Multi-Family Residential
out_mfr = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Multi-Family Residential/mfr.shp"
mfr.to_file(out_mfr)

#Single Family Residential
out_sfr = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Single Family Residential/sfr.shp"
sfr.to_file(out_sfr)

#Commercial
out_commercial = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Commercial/commercial.shp"
commercial.to_file(out_commercial)

#Open Space and Public Facilities
out_ospf = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Open Space and Public Facilities/ospf.shp"
ospf.to_file(out_ospf)

#Industrial
out_indus = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Industrial/indus.shp"
indus.to_file(out_indus)

#Parking
out_parking = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Pakring/parking.shp"
parking.to_file(out_parking)

#LA tracts 
out_tracts_inla = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Tracts In LA/tracts_inla.shp"
tracts_inla.to_file(out_tracts_inla)

#add new column in tracts file to record landuse percentage
tracts_inla["Single_Family_Residential"] = 0
tracts_inla["Multi_Family_Residential"] = 0
tracts_inla["Commercial"] = 0
tracts_inla["Open_Space_Public_Facilities"] = 0
tracts_inla["Industrial"] = 0
tracts_inla["Parking"] = 0

#calculate percentage

#read files using fiona
polygon0 = fiona.open(".\\landuseP\\Tracts In LA\\tracts_inla.shp")
polygon1 = fiona.open(".\\landuseP\\Single Family Residential\\sfr.shp")
polygon2 = fiona.open(".\\landuseP\\Multi-Family Residential\\mfr.shp")
polygon3 = fiona.open(".\\landuseP\\Commercial\\commercial.shp")
polygon4 = fiona.open(".\\landuseP\\Open Space and Public Facilities\\ospf.shp")
polygon5 = fiona.open(".\\landuseP\\Industrial\\indus.shp")
polygon6 = fiona.open(".\\landuseP\\Pakring\\parking.shp")

#extract geometry attribute
# 0. tracts
geom_p0 = [ shape(feat["geometry"]) for feat in polygon0 ]
# 1. single family residential
geom_p1 = [ shape(feat["geometry"]) for feat in polygon1 ]
# 2. multi-family residential
geom_p2 = [ shape(feat["geometry"]) for feat in polygon2 ]
# 3. commercial
geom_p3 = [ shape(feat["geometry"]) for feat in polygon3 ]
# 4. open space and public facilities
geom_p4 = [ shape(feat["geometry"]) for feat in polygon4 ]
# 5. industrial
geom_p5 = [ shape(feat["geometry"]) for feat in polygon5 ]
# 6. parking
geom_p6 = [ shape(feat["geometry"]) for feat in polygon6 ]

for i, g0 in enumerate(geom_p0):
    #percentage of each landuse type
    percentage1 = 0
    percentage2 = 0
    percentage3 = 0
    percentage4 = 0
    percentage5 = 0
    percentage6 = 0
    
    #calculate percentage of each landuse type
    for j, g1 in enumerate(geom_p1):
        if g0.intersects(g1):
            percentage1 = percentage1 + (g0.intersection(g1).area/g0.area)*100
            
    for j, g2 in enumerate(geom_p2):
        if g0.intersects(g2):
            percentage2 = percentage2 + (g0.intersection(g2).area/g0.area)*100
            
    for j, g3 in enumerate(geom_p3):
        if g0.intersects(g3):
            percentage3 = percentage3 + (g0.intersection(g3).area/g0.area)*100
            
    for j, g4 in enumerate(geom_p4):
        if g0.intersects(g4):
            percentage4 = percentage4 + (g0.intersection(g4).area/g0.area)*100
            
    for j, g5 in enumerate(geom_p5):
        if g0.intersects(g5):
            percentage5 = percentage5 + (g0.intersection(g5).area/g0.area)*100
            
    for j, g6 in enumerate(geom_p6):
        if g0.intersects(g6):
            percentage6 = percentage6 + (g0.intersection(g6).area/g0.area)*100
    
    
    #write percentages to tracts file
    tracts_inla.at[i, "Single_Family_Residential"] = percentage1
    tracts_inla.at[i, "Multi_Family_Residential"] = percentage2
    tracts_inla.at[i, "Commercial"] = percentage3
    tracts_inla.at[i, "Open_Space_Public_Facilities"] = percentage4
    tracts_inla.at[i, "Industrial"] = percentage5
    tracts_inla.at[i, "Parking"] = percentage6


#Write to new file
out_tracts_inla_landuse = r"/Users/cheny/OneDrive/Desktop/thesis/landuseP/Tracts in LA with Landuse/tracts_inla_with_landuse.shp"
tracts_inla.to_file(out_tracts_inla_landuse)

