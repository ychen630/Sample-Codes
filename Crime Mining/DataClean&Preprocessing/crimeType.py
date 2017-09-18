#This Program returns the types of Crime

import csv
import dateutil.parser as parser

with open('Book2.csv', 'r') as inp, open('book1_edit.csv', 'w', newline='') as out:
	writer = csv.writer(out)
	my_list=[]
	duplicate = 0
	for row in csv.reader(inp):
		a = row[8]
		if len(my_list) == 0:
			my_list.append(a)
		else:
			for i in my_list:
				if i == a:
					duplicate = 1
			if duplicate == 0:
				my_list.append(a)
			duplicate = 0
	print(my_list)
	print(len(my_list))
			
		#print(a)
		#if a == "THEFT":
		#if a == "BATTERY":
		#if a == "CRIMINAL DAMAGE":
		#if a == "NARCOTICS":
		#if a == "OTHER OFFENSE":
			#writer.writerow(row)
