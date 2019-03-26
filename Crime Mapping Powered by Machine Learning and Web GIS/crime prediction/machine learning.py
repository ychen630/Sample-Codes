
'''
Yixin Chen
Mar 24, 2019
Model training and parameter tuning
XGBoost and Random forest
'''

# import libraries
import pandas as pd
import numpy as np
import geopandas as gpd
from shapely.geometry import Point
import os
import matplotlib.pyplot as plt
import seaborn as sns

#read data
d17nr=pd.read_csv('.\\crime_data\\bglry17_reduced_with_landuse.csv')
d17nr.head()
#d17nr.info()

#train test split, with landuse
from sklearn.model_selection import train_test_split

X = d17nr.drop(columns=['GEOID10', 'BGLRY17'], axis = 1)
y = d17nr['BGLRY17']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

#random forest regressor
from sklearn.ensemble import RandomForestRegressor

rfr = RandomForestRegressor(n_estimators = 10, random_state = 0)
rfr.fit(X_train, y_train)

#feature importance
print(rfr.feature_importances_)

# Predicting a new result
y_pred = rfr.predict(X_test)

#evalutate performance
from sklearn.metrics import mean_squared_error, r2_score

# The mean squared error
print("Mean squared error:", mean_squared_error(y_test, y_pred))
# Explained variance score: 1 is perfect prediction
print('R squared:', r2_score(y_test, y_pred))

# compute with formulas from the theory
SS_Residual = sum((y_test-y_pred)**2)
SS_Total = sum((y_test-np.mean(y_test))**2)
r_squared = 1 - (float(SS_Residual))/SS_Total
adjusted_r_squared = 1 - (1-r_squared)*(len(y_test)-1)/(len(y_test)-X_test.shape[1]-1)
print("Adjusted R squared", adjusted_r_squared)

#train model without landuse data
X = d17nr.drop(columns=['GEOID10', 'BGLRY17', 'Multi_Fami', 'Commercial', 'Industrial'], axis = 1)
y = d17nr['BGLRY17']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

rfr = RandomForestRegressor(n_estimators = 10, random_state = 0)
rfr.fit(X_train, y_train)

print(rfr.feature_importances_)

# Predicting a new result
y_pred = rfr.predict(X_test)

# The mean squared error
print("Mean squared error:", mean_squared_error(y_test, y_pred))
# Explained variance score: 1 is perfect prediction
print('R squared:', r2_score(y_test, y_pred))


# Data with landuse performs better

#xgboost
from xgboost import XGBRegressor
from sklearn.metrics import accuracy_score

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

X = d17nr.drop(columns=['GEOID10', 'BGLRY17'], axis = 1)
y = d17nr['BGLRY17']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

model = XGBRegressor()
model.fit(X_train, y_train)

y_pred = model.predict(X_test)
#accuracy = accuracy_score(y_test, y_pred)

f_imp = model.feature_importances_

# evalutate model performance
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error
from sklearn.metrics import r2_score

print ("mae: {:,}".format(mean_absolute_error(y_test, y_pred)))
print ("mse: {:,}".format(mean_squared_error(y_test, y_pred)))
print ("r2:  {:,}".format(r2_score(y_test, y_pred)))

# plot feature importance
f_imp_df = pd.DataFrame({'feature': X_train.columns, 'imp': f_imp}).sort_values(by='imp', ascending = False)
f, ax = plt.subplots(figsize=(6, 12))
sns.barplot(x='imp', y='feature', data=f_imp_df, color="darkslateblue", alpha=.7, orient='h', ax=ax)
plt.show()

#xgboost performs better than random forest

#tune learning_rate, max_depth and n_estimators, with landuse data
X = d17nr.drop(columns=['GEOID10', 'BGLRY17'], axis = 1)
y = d17nr['BGLRY17']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

# specify a range of parameters for Grid search of best parameters
params_xgb = {"learning_rate": [0.0001, 0.001, 0.01, 0.1, 0.2, 0.3],
              "max_depth": [2, 4, 6, 8],
                 "n_estimators": [50, 100, 150, 200]}

# fixed parameters
regr_xgb = XGBRegressor(random_state=13,
                        n_jobs=-1,
                        booster='gbtree')

# set up cross validation to search for best parameters
from sklearn.model_selection import GridSearchCV
gs_xgb = GridSearchCV(estimator=regr_xgb, param_grid=params_xgb, scoring='r2', n_jobs=-1, cv=5, verbose=0)

# search for best parameters, results as final model
gs_xgb.fit(X_train, y_train)
gs_xgb.best_estimator_

# make predictions using test set, get feature importance
regr_xgb = gs_xgb.best_estimator_
regr_xgb_pred = regr_xgb.predict(X_test)
f_imp = regr_xgb.feature_importances_

# evalutate model performance
print ("mae: {:,}".format(mean_absolute_error(y_test, regr_xgb_pred)))
print ("mse: {:,}".format(mean_squared_error(y_test, regr_xgb_pred)))
print ("r2:  {:,}".format(r2_score(y_test, regr_xgb_pred)))

# plot feature importance
import seaborn as sns
import matplotlib.pyplot as plt
f_imp_df = pd.DataFrame({'feature': X_train.columns, 'imp': f_imp}).sort_values(by='imp', ascending = False)
f, ax = plt.subplots(figsize=(6, 12))
sns.barplot(x='imp', y='feature', data=f_imp_df, color="darkslateblue", alpha=.7, orient='h', ax=ax)
plt.show()

#without landuse data
X = d17nr.drop(columns=['GEOID10', 'BGLRY17', 'Multi_Fami', 'Commercial', 'Industrial'], axis = 1)
y = d17nr['BGLRY17']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

model = XGBRegressor()
model.fit(X_train, y_train)

y_pred = model.predict(X_test)

# evalutate model performance
print ("mae: {:,}".format(mean_absolute_error(y_test, y_pred)))
print ("mse: {:,}".format(mean_squared_error(y_test, y_pred)))
print ("r2:  {:,}".format(r2_score(y_test, y_pred)))


#tune learning_rate, max_depth and n_estimators, without landuse data
X = d17nr.drop(columns=['GEOID10', 'BGLRY17', 'Multi_Fami', 'Commercial', 'Industrial'], axis = 1)
y = d17nr['BGLRY17']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

# specify a range of parameters for Grid search of best parameters
params_xgb = {"learning_rate": [0.0001, 0.001, 0.01, 0.1, 0.2, 0.3],
              "max_depth": [2, 4, 6, 8],
                 "n_estimators": [50, 100, 150, 200]}

# fixed parameters
regr_xgb = XGBRegressor(random_state=13,
                        n_jobs=-1,
                        booster='gbtree')

# set up cross validation to search for best parameters
from sklearn.model_selection import GridSearchCV
gs_xgb = GridSearchCV(estimator=regr_xgb, param_grid=params_xgb, scoring='r2', n_jobs=-1, cv=5, verbose=0)

# search for best parameters, results as final model
gs_xgb.fit(X_train, y_train)
gs_xgb.best_estimator_

# make predictions using test set, get feature importance
regr_xgb = gs_xgb.best_estimator_
regr_xgb_pred = regr_xgb.predict(X_test)
f_imp = regr_xgb.feature_importances_

# evalutate model performance
print ("mae: {:,}".format(mean_absolute_error(y_test, regr_xgb_pred)))
print ("mse: {:,}".format(mean_squared_error(y_test, regr_xgb_pred)))
print ("r2:  {:,}".format(r2_score(y_test, regr_xgb_pred)))

# plot feature importance
import seaborn as sns
import matplotlib.pyplot as plt
f_imp_df = pd.DataFrame({'feature': X_train.columns, 'imp': f_imp}).sort_values(by='imp', ascending = False)
f, ax = plt.subplots(figsize=(6, 12))
sns.barplot(x='imp', y='feature', data=f_imp_df, color="darkslateblue", alpha=.7, orient='h', ax=ax)
plt.show()


#use the best XGboost model to predit crime in other cities in the LA county

# acs data for predicting in other cities

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

acs16.head()

acs16.drop(list(acs16.filter(regex = 'Geo_')), axis = 1, inplace = True)
acs16.head()

acs16.set_index('GEOID10', inplace = True)
acs16.head()


# In[17]:
# drop duplicate columns on Total Population, area land,% Total Population: Female,  
# % Occupied Housing Units: Renter Occupied, % Housing Units: Vacant
# 84 acs features - 7 duplicates = 77 acs features
acs16 = acs16.drop(columns=['SE_T002_001', 'SE_T004_001', 'SE_T013_001',
             'SE_T002_003', 
             'PCT_SE_T004_003',
             'PCT_SE_T094_003',
             'PCT_SE_T095_003'], axis = 1)

# remove features less correlated with burgluary
# 77 features -12 small r = 65 features
acs16 = acs16.drop(columns=['SE_T003_003', 'PCT_SE_T003_002',
       'PCT_SE_T003_003', 'PCT_SE_T004_002', 'SE_T013_006', 'PCT_SE_T013_006',
       'SE_T025_003', 'SE_T033_003', 'SE_T033_007', 'PCT_SE_T033_003',
       'PCT_SE_T033_006', 'PCT_SE_T094_002'], axis = 1)

# remove features highly correlated with total population
# 65- 4 = 61 features
acs16 = acs16.drop(columns=['SE_T004_002', 'SE_T004_003', 'SE_T033_001',
       'SE_T118_001'], axis = 1)

len(acs16.columns)
acs16.head()

LACounty_pred = regr_xgb.predict(acs16)


#for i in range(len(LACounty_pred)):
acs16['Burglar'] = LACounty_pred
LACounty_pred

acs16.head()


crimePredit = acs16.loc[:, ['Burglar']]
crimePredit

type(crimePredit)

#write predicted data to file
crimePredit.to_csv('.\\crime_data\\predictCrimeLACounty.csv')

