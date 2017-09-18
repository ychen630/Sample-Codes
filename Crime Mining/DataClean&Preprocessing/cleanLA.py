#This Program cleans the raw crime data of Los Angeles

#This program extract the data from 01/01/2011 to 12/31/1015

#The GeoReference in the raw data of Los Angeles is listed as "(Latitude, Longitude)"
#This program split the GeoReference to "Latitude", "Longitude"

#This program also removes data without Latitude or Longitude information

#C:\Users\xin\AppData\Local\Programs\Python\Python36-32\python.exe dd.py

import csv
import dateutil.parser as parser

with open('Book1.csv', 'r') as inp, open('book1_edit.csv', 'w', newline='') as out:
	writer = csv.writer(out)
	for row in csv.reader(inp):
		a = parser.parse(row[2]).year
		row[25] = row[25].replace('(','')
		row[25] = row[25].replace(')','')
		row[25] = row[25].replace(',','')
		row[25:26] = row[25].split(' ')
		b = row[25]
		if a > 2010 and a < 2016:
			if b != '0':
				writer.writerow(row)