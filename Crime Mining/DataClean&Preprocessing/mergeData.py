import csv
import array
import os
import pandas as pd

a = pd.read_csv('C:/Users/xin/Desktop/dm/LosAngelesBG1to1/bg.csv')
print ("file 1 loaded")
b = pd.read_csv("C:/Users/xin/Desktop/dm/LosAngelesBG1to1/census.csv")
print ("file 2 loaded")
b = b.dropna(axis=1)
merged = a.merge(b, on='Geo_FIPS')
print ("merged")
merged.to_csv("C:/Users/xin/Desktop/dm/LosAngelesBG1to1/output.csv", index=False)