'''
Yixin Chen
Mar 24, 2019
Feature engineering
'''

# import libraries
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import datetime 
import os

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

#bglry17_with_landuse_noGeometry.csv

# Importing the dataset
d17 = pd.read_csv('.\\crime_data\\bglry17_with_landuse_noGeometry.csv')

# drop useless columns start with Geo
d17.drop(list(d17.filter(regex = 'Geo_')), axis = 1, inplace = True)
d17.drop(columns=d17.columns[0], axis = 1, inplace = True)
d17.set_index('GEOID10', inplace = True)

# correlation
d17.corr()

# drop duplicate columns on Total Population, area land,% Total Population: Female,  
# % Occupied Housing Units: Renter Occupied, % Housing Units: Vacant
# 84 acs features + 6 landuse features- 7 duplicates = 83 acs features
d17n = d17.drop(columns=['SE_T002_001', 'SE_T004_001', 'SE_T013_001',
             'SE_T002_003', 
             'PCT_SE_T004_003',
             'PCT_SE_T094_003',
             'PCT_SE_T095_003'], axis = 1)
len(d17n.columns)
# pearson correlation matrix
pearson = d17n.corr()
pearson

# Feature Selection According to Correlation, exclude features with correlation coefficient to label less than .05
selected=pearson.loc[(pearson['BGLRY17']<=.05)&(pearson['BGLRY17']>=-.05), :]
selected.index

# remove features less correlated with burgluary
# 83 features -15 small r = 68 features
d17n = d17n.drop(columns=['Single_Fam', 'Open_Space', 'Parking', 'SE_T003_003', 'PCT_SE_T003_002',
       'PCT_SE_T003_003', 'PCT_SE_T004_002', 'SE_T013_006', 'PCT_SE_T013_006',
       'SE_T025_003', 'SE_T033_003', 'SE_T033_007', 'PCT_SE_T033_003',
       'PCT_SE_T033_006', 'PCT_SE_T094_002'], axis = 1)
len(d17n.columns)
d17n.head()

# new pearson correlation matrix
pearson = d17n.corr()
pearson

# remove features highly correlated with total population
# 68- 4 = 64 features
selected = pearson.loc[(pearson['SE_T001_001']>.90)|(pearson['SE_T001_001']<-.90), :]
selected.index

d17n = d17n.drop(columns=['SE_T004_002', 'SE_T004_003', 'SE_T033_001',
       'SE_T118_001'], axis = 1)

len(d17n.columns)
d17n.head()

d17n.corr().index

# Recursive Feature Elimination
names = ['Multi_Fami', 'Commercial', 'Industrial', 'SE_T001_001',
       'SE_T002_002', 'SE_T003_001', 'SE_T003_002', 'SE_T012_001',
       'SE_T012_002', 'SE_T012_003', 'SE_T013_002', 'SE_T013_003',
       'SE_T013_004', 'SE_T013_005', 'SE_T013_007', 'SE_T013_008',
       'PCT_SE_T013_002', 'PCT_SE_T013_003', 'PCT_SE_T013_004',
       'PCT_SE_T013_005', 'PCT_SE_T013_007', 'PCT_SE_T013_008', 'SE_T025_001',
       'SE_T025_002', 'SE_T025_004', 'SE_T025_005', 'SE_T025_006',
       'SE_T025_007', 'SE_T025_008', 'PCT_SE_T025_002', 'PCT_SE_T025_003',
       'PCT_SE_T025_004', 'PCT_SE_T025_005', 'PCT_SE_T025_006',
       'PCT_SE_T025_007', 'PCT_SE_T025_008', 'SE_T033_002', 'SE_T033_004',
       'SE_T033_005', 'SE_T033_006', 'PCT_SE_T033_002', 'PCT_SE_T033_004',
       'PCT_SE_T033_005', 'PCT_SE_T033_007', 'SE_T057_001', 'SE_T083_001',
       'SE_T157_001', 'SE_T094_001', 'SE_T094_002', 'SE_T094_003',
       'SE_T095_001', 'SE_T095_002', 'SE_T095_003', 'PCT_SE_T095_002',
       'SE_T118_002', 'SE_T118_003', 'SE_T118_004', 'SE_T118_005',
       'PCT_SE_T118_002', 'PCT_SE_T118_003', 'PCT_SE_T118_004',
       'PCT_SE_T118_005', 'RaceDivIndx', 'NhoodDisIdx']

X = d17n.loc[:, names].values
Y = d17n.loc[:,'BGLRY17'].values

#Recursive Feature Elimination
#use random forest regression as the model
from sklearn.feature_selection import RFE

from sklearn.ensemble import RandomForestRegressor
lr = RandomForestRegressor(n_estimators = 10, random_state = 0)

#rank all features, i.e continue the elimination until the last one
rfe = RFE(lr, n_features_to_select=1)
rfe.fit(X,Y)
 
print ("Features sorted by their rank:")
print (sorted(zip(map(lambda x: round(x, 4), rfe.ranking_), names)))

d17n.to_csv('.\\crime_data\\bglry17_reduced_with_landuse.csv')