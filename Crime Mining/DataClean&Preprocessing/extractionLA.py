#This Program cleans the raw crime data of Chicago
#This program extract the data from 01/01/2011 to 12/31/1015

#C:\Users\xin\AppData\Local\Programs\Python\Python36-32\python.exe dr.py

import csv
import dateutil.parser as parser

with open('losangeles.csv', 'r') as inp, open('Los_Angeles_2015.csv', 'w', newline='') as out:
	writer = csv.writer(out)
	for row in csv.reader(inp):
		a = parser.parse(row[2]).year
		if a == 2015:
			writer.writerow(row)
