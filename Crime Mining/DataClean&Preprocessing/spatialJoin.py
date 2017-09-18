#Description:
#Overlay Census 2010 Block Group with Chicago Crime location data

#import system modules
import arcpy
from arcpy import env
import csv
import os
# Overwrite pre-existing files
arcpy.env.overwriteOutput = True

##set up parameters
arcpy.env.workspace="C:/Users/xin/Desktop/dm"
In_Table="crimes_Chicago_2011_2015.csv"
spRef="tl_2010_17_bg10.prj"
###arcpy.CreateFileGDB_management("C:\Users\xin\Desktop\dm", "crimes.gdb")
print("hhh")
arcpy.MakeXYEventLayer_management(In_Table,"Longitude","Latitude", "/crimes.gdb/crime_Chicago",spRef)
print("step1")
arcpy.CopyFeatures_management("/crimes.gdb/crime_Chicago", "/crimes.gdb/crime_ChicagoPoints11_15" )
print("step2")

TargetFC="/crimes.gdb/crime_ChicagoPoints11_15"
JoinFC="/crimes.gdb/tl_2010_17_bg10_Clip"
OutFC1="/crimes.gdb/CrimeChicagoBG_OneToOne"
OutFC2="/crimes.gdb/CrimeChicagoBG_OneToMany"
arcpy.SpatialJoin_analysis(TargetFC,JoinFC, OutFC1, "JOIN_ONE_TO_ONE","KEEP_ALL","","WITHIN")
 
arcpy.SpatialJoin_analysis(TargetFC,JoinFC, OutFC2, "JOIN_ONE_TO_MANY","KEEP_ALL","","WITHIN")
