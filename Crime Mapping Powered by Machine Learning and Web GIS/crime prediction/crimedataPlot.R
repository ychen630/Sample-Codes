# Program Description ----------------------------------------------------------
# Merge crime counts with 2015 ACS 5-year summary files at block group level.  #
# Calculate crime rates, and examine correlation of crime rate and BG vars.    #
# Los Angeles City                                            #
# ---------------------------------------------------------------------------- #
# Version: 1.0                                                                 #
# Date Created: 11/23/2018                                                       #
# Author: Yixin Chen                                                           #
################################################################################

# Libraries & data---------------------------------------------------------
library("dplyr")
library("ggplot2")
setwd("Your Path")

# # Required input data files-----------------------------------------------
LosAngelesACS   <- read.csv("CensusLosAngeles2011-2015.csv")
LosAngelesCrime <- read.csv("CrimeBGLosAngeles2011-2015.csv")

# # Data Management --------------------------------------------------------
# Rename merge key
names(LosAngelesACS)[names(LosAngelesACS) == 'Geo_FIPS'] <- 'GEOID10'


# Merge crime data with ACS data
LosAngeles<-left_join(LosAngelesCrime, LosAngelesACS, by = "GEOID10")

# compute crime rate==count/1000 population
LosAngeles$CrimeRate<-1000*(LosAngeles$join_Count/LosAngeles$SE_T001_001)
# Median household income in 1000 dollars
LosAngeles$MedInc   <-LosAngeles$SE_T057_001/1000
# education level
LosAngeles$CollegePlus  <-LosAngeles %>% select(PCT_SE_T025_005:PCT_SE_T025_008) %>% rowSums(na.rm=TRUE) 
LosAngeles$SomeColPlus  <-LosAngeles %>% select(PCT_SE_T025_004:PCT_SE_T025_008) %>% rowSums(na.rm=TRUE) 

#find quantiles 326525, 629075
sapply(LosAngeles, quantile, na.rm=TRUE)
# create 3 median house value categories according to quantile, top 25% as high, bottom 25% as low
attach(LosAngeles)
LosAngeles$MedHV[SE_T101_001 > 629075] <- "High"
LosAngeles$MedHV[SE_T101_001 > 326525 & SE_T101_001 <= 629075] <- "Middle"
LosAngeles$MedHV[SE_T101_001 <=326525] <- "Low"
detach(LosAngeles)


# # Plots-------------- --------------------------------------------------------
# Scatterplot Add fit lines

# plot1: income and crime
ggplot(LosAngeles, aes(x=MedInc, y=CrimeRate)) +
  geom_point(shape=1,colour="red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles Household Income and Crime",
       x="Median Household Income in 1000 Dollars", y = "Crime Rate (count/1K population)")+
  ylim(0, 1000)


# plot1b: income and crime by med house value
ggplot(LosAngeles, aes(x=MedInc, y=CrimeRate, colour=MedHV)) +
  geom_point(shape=1) +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles Household Income and Crime by Median House Value",
       x="Median Household Income in 1000 Dollars", y = "Crime Rate (count/1K population)")+
  ylim(0, 1000)+
  facet_grid (. ~ MedHV)

# plot2: house value and crime
ggplot(LosAngeles, aes(x=SE_T101_001, y=CrimeRate)) +
  geom_point(shape=1,colour="red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles House Value and Crime",
       x="Median House Value", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)

# plot 2b: Gross rent
ggplot(LosAngeles, aes(x=SE_T104_001, y=CrimeRate)) +
  geom_point(shape=1,colour = "red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles Gross Rent and Crime",
       x="Median Gross Rent in 1000 dollars", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)

# plot3: Education and crime rate
ggplot(LosAngeles, aes(x=CollegePlus, y=CrimeRate)) +
  geom_point(shape=1,colour="red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles Education and Crime ",
       x="% college degree or higher", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)

# plot3b: Education and crime rate by Med house value
ggplot(LosAngeles, aes(x=CollegePlus, y=CrimeRate,colour=MedHV)) +
  geom_point(shape=1) +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles Education and Crime Rate by Median House Value",
       x="% college degree or higher", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)+
  facet_grid (. ~ MedHV)

###some college or high school does not have a large correlation with crime rate
# ggplot(LosAngeles, aes(x=SomeColPlus, y=CrimeRate)) +
#   geom_point(shape=1) +    # Use hollow circles
#   geom_smooth(method="auto",   # Add linear regression line
#               se=FALSE) +   # Don't add shaded confidence region
#   labs(title="Los Angeles City Crime Analysis",
#        x="% Some college or higher", y = "Crime Rate (count/1K population)")+
#   ylim(0, 1000)


# plot3c: dropout rate and crime rate 
ggplot(LosAngeles, aes(x=PCT_SE_T030_002, y=CrimeRate)) +
  geom_point(shape=1,colour="red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles High School drop out rate and crime",
       x="High School drop out rate among 16-19 year olds", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)



# plot 4: unemployment and crime
ggplot(LosAngeles, aes(x=PCT_SE_T037_003, y=CrimeRate)) +
  geom_point(shape=1,colour = "red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles umemployment rate vs crime",
       x="Unemployment rate", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)+
  xlim(0,50)
  

# plot 5: age of male population and crime
ggplot(LosAngeles, aes(x=SE_T012_002, y=CrimeRate)) +
  geom_point(shape=1,colour = "red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles male age vs crime",
       x="Median age of male population", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)

# plot 5b: age of female population and crime
ggplot(LosAngeles, aes(x=SE_T012_003, y=CrimeRate)) +
  geom_point(shape=1,colour = "red") +    # Use hollow circles
  geom_smooth(method="auto",   # Add linear regression line
              se=FALSE) +   # Don't add shaded confidence region
  labs(title="Los Angeles female age vs crime",
       x="Median age of female population", y = "Crime Rate(count/1K population)")+
  ylim(0, 1000)

dev.off() 
