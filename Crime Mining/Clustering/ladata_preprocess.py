import csv
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

#Primary Type,Description,,Date,Time,Block,Chicago,IL,,Latitude,Longitude

def process_main_csv(num):
#splits the data into years and preprocesses into a uniform format
    with open('Crime_Data_From_2010_to_Present.csv') as f:
        with open('Formatted_Crime_Data_201{}.csv'.format(num), "wb") as csv_file:
            writer = csv.writer(csv_file, delimiter=',')
            #line = f.readline()
            #writer.writerow(('Crime Code', 'Crime Code Desc', '', 'Date Occurred', 'Time Occurred', 'LOCATION', 'City', 'State', '','Latitude','Longitude'))
            for line in csv.reader(f):
                if line[2][-1] == '{}'.format(num):
                    writer.writerow((line[7],line[8],'',line[2], line[3]," ".join(line[23].split()),'Los Angeles','CA','',line[-1].split(',')[0].strip('('),line[-1].split(',')[1].strip(')') if len(line[-1].split(',')) > 1 else 'Longitude'))
    print 'finished 201{}'.format(num)

def process_year_csv(num):
#takes the formatted data csv and then:
#   changes crimes into an integer
#   removes other fields
	list_of_crimes = ['', 'THEFT OF IDENTITY', 'DISCHARGE FIREARMS/SHOTS FIRED', 'DISTURBING THE PEACE', 'BATTERY POLICE (SIMPLE)', 'CHILD STEALING', 'DEFRAUDING INNKEEPER/THEFT OF SERVICES, $400 & UNDER', 'CHILD NEGLECT (SEE 300 W.I.C.)', 'DOCUMENT WORTHLESS ($200.01 & OVER)', 'BURGLARY, ATTEMPTED', 'GRAND THEFT / INSURANCE FRAUD', 'TILL TAP - ATTEMPT', 'EXTORTION', 'TELEPHONE PROPERTY - DAMAGE', 'VANDALISM - FELONY ($400 & OVER, ALL CHURCH VANDALISMS)', 'GRAND THEFT / AUTO REPAIR', 'THEFT, COIN MACHINE - PETTY ($950 & UNDER)', 'BATTERY WITH SEXUAL CONTACT', 'CRUELTY TO ANIMALS', 'SPOUSAL (COHAB) ABUSE - AGGRAVATED ASSAULT', 'DRUNK ROLL', 'ASSAULT WITH DEADLY WEAPON, AGGRAVATED ASSAULT', 'CRIMINAL THREATS - NO WEAPON DISPLAYED', 'VEHICLE - STOLEN', 'DOCUMENT FORGERY / STOLEN FELONY', 'CHILD ABUSE (PHYSICAL) - SIMPLE ASSAULT', 'CRIMINAL HOMICIDE', 'LETTERS, LEWD', 'PICKPOCKET, ATTEMPT', 'BUNCO, PETTY THEFT', 'VIOLATION OF COURT ORDER', 'BURGLARY', 'THEFT, COIN MACHINE - ATTEMPT', 'ORAL COPULATION', 'BIGAMY', 'VIOLATION OF RESTRAINING ORDER', 'THEFT PLAIN - ATTEMPT', 'SHOTS FIRED AT MOVING VEHICLE, TRAIN OR AIRCRAFT', 'BRIBERY', 'PANDERING', 'INDECENT EXPOSURE', 'COUNTERFEIT', 'SEX, UNLAWFUL', 'CREDIT CARDS, FRAUD USE ($950 & UNDER', 'BUNCO, GRAND THEFT', 'THEFT, PERSON', 'THEFT FROM PERSON - ATTEMPT', 'TILL TAP - GRAND THEFT ($950.01 & OVER)', 'FALSE IMPRISONMENT', 'CONSPIRACY', 'KIDNAPPING - GRAND ATTEMPT', 'SPOUSAL(COHAB) ABUSE - SIMPLE ASSAULT', 'MANSLAUGHTER, NEGLIGENT', 'RAPE, FORCIBLE', 'RESISTING ARREST', 'PICKPOCKET', 'THROWING OBJECT AT MOVING VEHICLE', 'BURGLARY FROM VEHICLE', 'BUNCO, ATTEMPT', 'LYNCHING', 'KIDNAPPING', 'OTHER MISCELLANEOUS CRIME', 'BRANDISH WEAPON', 'SHOPLIFTING-GRAND THEFT ($950.01 & OVER)', 'TRESPASSING', 'ROBBERY', 'EMBEZZLEMENT, GRAND THEFT ($950.01 & OVER)', 'CREDIT CARDS, FRAUD USE ($950.01 & OVER)', 'FALSE POLICE REPORT', 'SEXUAL PENTRATION WITH A FOREIGN OBJECT', 'CONTEMPT OF COURT', 'DRUGS, TO A MINOR', 'INCITING A RIOT', 'VEHICLE - ATTEMPT STOLEN', 'SODOMY/SEXUAL CONTACT B/W PENIS OF ONE PERS TO ANUS OTH', 'DRUNK ROLL - ATTEMPT', 'CHILD ABANDONMENT', 'PURSE SNATCHING', 'THREATENING PHONE CALLS/LETTERS', 'LYNCHING - ATTEMPTED', 'BIKE - STOLEN', 'VANDALISM - MISDEAMEANOR ($399 OR UNDER)', 'BATTERY ON A FIREFIGHTER', 'LEWD CONDUCT', 'CHILD, CRIME AGAINST', 'EMBEZZLEMENT, PETTY THEFT ($950 & UNDER)', 'THEFT FROM MOTOR VEHICLE - ATTEMPT', 'FAILURE TO YIELD', 'STALKING', 'BURGLARY FROM VEHICLE, ATTEMPTED', 'RAPE, ATTEMPTED', 'SHOTS FIRED AT INHABITED DWELLING', 'VIOLATION OF TEMPORARY RESTRAINING ORDER', 'DOCUMENT WORTHLESS ($200 & UNDER)', 'BEASTIALITY, CRIME AGAINST NATURE SEXUAL ASSLT WITH ANIM', 'ATTEMPTED ROBBERY', 'PROWLER', 'BOAT - STOLEN', 'PIMPING', 'ILLEGAL DUMPING', 'PEEPING TOM', 'THEFT, COIN MACHINE - GRAND ($950.01 & OVER)', 'TILL TAP - PETTY ($950 & UNDER)', 'THEFT-GRAND ($950.01 & OVER)EXCPT,GUNS,FOWL,LIVESTK,PROD', 'THEFT PLAIN - PETTY ($950 & UNDER)', 'OTHER ASSAULT', 'THEFT FROM MOTOR VEHICLE - GRAND ($400 AND OVER)', 'DISHONEST EMPLOYEE - PETTY THEFT', 'CONTRIBUTING', 'BOMB SCARE', 'SHOPLIFTING - ATTEMPT', 'RECKLESS DRIVING', 'DRIVING WITHOUT OWNER CONSENT (DWOC)', 'DISRUPT SCHOOL', 'UNAUTHORIZED COMPUTER ACCESS', 'ARSON', 'SHOPLIFTING - PETTY THEFT ($950 & UNDER)', 'ASSAULT WITH DEADLY WEAPON ON POLICE OFFICER', 'THEFT FROM MOTOR VEHICLE - PETTY ($950 & UNDER)', 'PURSE SNATCHING - ATTEMPT', 'CHILD ANNOYING (17YRS & UNDER)', 'DEFRAUDING INNKEEPER/THEFT OF SERVICES, OVER $400', 'PETTY THEFT - AUTO REPAIR', 'WEAPONS POSSESSION/BOMBING', 'CHILD ABUSE (PHYSICAL) - AGGRAVATED ASSAULT', 'DISHONEST EMPLOYEE - GRAND THEFT', 'BATTERY - SIMPLE ASSAULT']
	
    with open('Formatted_Crime_Data_201{}.csv'.format(num)) as orig_file:
		with open('processed_data_201{}.csv'.format(num), "wb") as csv_file:
			writer = csv.writer(csv_file, delimiter=',')
			for line in csv.reader(orig_file):
				writer.writerow((list_of_crimes.index(line[1]),line[-2],line[-1]))
	print 'finished 201{}'.format(num)

def collect_crime_names(num):
#used to get the set of crimes for the previous method, not used afterwards
	crimes_set = set()
	with open('Formatted_Crime_Data_201{}.csv'.format(num)) as csv_file:
		#line = f.readline()
		#writer.writerow(('Crime Code', 'Crime Code Desc', '', 'Date Occurred', 'Time Occurred', 'LOCATION', 'City', 'State', '','Latitude','Longitude'))
		for line in csv.reader(csv_file):
			print line[1]
			crimes_set.add(line[1])
	print crimes_set
	print 'finished 201{}'.format(num)

def plot_cluster_csv3d(num):
    crime_list = []
    latitude_list = []
    longitude_list = []
    with open('processed_data_201{}.csv'.format(num)) as csv_file:
        for _ in range(6000):
            line = csv_file.readline()
            print line.strip('\n')
            line_split = line.split(',')
            crime = float(line_split[0])
            lat = float(line_split[1])
            longitude = float(line_split[2])
            if lat != 0 and longitude != 0:
                crime_list.append(crime)
                latitude_list.append(lat)
                longitude_list.append(longitude)
            print crime, lat, longitude
            print ''
        print len(crime_list), len(latitude_list), len(longitude_list)

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    n = 100

    # For each set of style and range settings, plot n random points in the box
    # defined by x in [23, 32], y in [0, 100], z in [zlow, zhigh].
    for c, m, zlow, zhigh in [('r', 'o', -50, -25), ('b', '^', -30, -5)]:
        # xs = randrange(n, 23, 32)
        # ys = randrange(n, 0, 100)
        # zs = randrange(n, zlow, zhigh)
        # ax.scatter(xs, ys, zs, c=c, marker=m)
        xs = latitude_list
        ys = longitude_list
        zs = crime_list
    ax.scatter(xs, ys, zs)
    ax.set_xlabel('Latitude')
    ax.set_ylabel('Longitude')
    ax.set_zlabel('Crime ID')

    plt.show()

def plot_cluster_csv(num):
    crime_list = []
    latitude_list = []
    longitude_list = []
    with open('processed_data_201{}.csv'.format(num)) as csv_file:
        for _ in range(6000):
            line = csv_file.readline()
            print line.strip('\n')
            line_split = line.split(',')
            crime = float(line_split[0])
            lat = float(line_split[1])
            longitude = float(line_split[2])
            if lat != 0 and longitude != 0:
                crime_list.append(crime)
                latitude_list.append(lat)
                longitude_list.append(longitude)
            print crime, lat, longitude
            print ''
        print len(crime_list), len(latitude_list), len(longitude_list)

    df = pd.DataFrame(dict(x=latitude_list, y=longitude_list, label=crime_list))

    groups = df.groupby('label')

    # Plot
    fig, ax = plt.subplots()
    ax.margins(0.05) # Optional, just adds 5% padding to the autoscaling
    for name, group in groups:
        ax.plot(group.x, group.y, marker='.', linestyle='', ms=12, label=name)
    ax.legend()

    plt.show()
if __name__ == '__main__':
    for year in range(8):
        process_main_csv(year)
    process_to_make_cluster_csv(0)
	#collect_crime_names(0)
    # process(0)
	#read_ex()
    plot_cluster_csv(0)
    plot_cluster_csv3d

