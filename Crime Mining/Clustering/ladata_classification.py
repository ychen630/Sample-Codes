from sklearn.model_selection import cross_val_score
import csv
import matplotlib.pyplot as plt
import numpy as np
import matplotlib
from matplotlib import cm
from sklearn.linear_model import SGDClassifier
from sklearn.linear_model import LinearRegression
from sklearn.mixture import GaussianMixture
from sklearn.decomposition import PCA
from mpl_toolkits.mplot3d import Axes3D

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
    list_of_crimes = ['', 'THEFT OF IDENTITY', 'DISCHARGE FIREARMS/SHOTS FIRED', 'DISTURBING THE PEACE', 'BATTERY POLICE (SIMPLE)', 'CHILD STEALING', 'DEFRAUDING INNKEEPER/THEFT OF SERVICES, $400 & UNDER', 'CHILD NEGLECT (SEE 300 W.I.C.)', 'DOCUMENT WORTHLESS ($200.01 & OVER)', 'BURGLARY, ATTEMPTED', 'GRAND THEFT / INSURANCE FRAUD', 'TILL TAP - ATTEMPT', 'EXTORTION', 'TELEPHONE PROPERTY - DAMAGE', 'VANDALISM - FELONY ($400 & OVER, ALL CHURCH VANDALISMS)', 'GRAND THEFT / AUTO REPAIR', 'THEFT, COIN MACHINE - PETTY ($950 & UNDER)', 'BATTERY WITH SEXUAL CONTACT', 'CRUELTY TO ANIMALS', 'SPOUSAL (COHAB) ABUSE - AGGRAVATED ASSAULT', 'DRUNK ROLL', 'ASSAULT WITH DEADLY WEAPON, AGGRAVATED ASSAULT', 'CRIMINAL THREATS - NO WEAPON DISPLAYED', 'VEHICLE - STOLEN', 'DOCUMENT FORGERY / STOLEN FELONY', 'CHILD ABUSE (PHYSICAL) - SIMPLE ASSAULT', 'CRIMINAL HOMICIDE', 'LETTERS, LEWD', 'PICKPOCKET, ATTEMPT', 'BUNCO, PETTY THEFT', 'VIOLATION OF COURT ORDER', 'BURGLARY', 'THEFT, COIN MACHINE - ATTEMPT', 'ORAL COPULATION', 'BIGAMY', 'VIOLATION OF RESTRAINING ORDER', 'THEFT PLAIN - ATTEMPT', 'SHOTS FIRED AT MOVING VEHICLE, TRAIN OR AIRCRAFT', 'BRIBERY', 'PANDERING', 'INDECENT EXPOSURE', 'COUNTERFEIT', 'SEX, UNLAWFUL', 'CREDIT CARDS, FRAUD USE ($950 & UNDER', 'BUNCO, GRAND THEFT', 'THEFT, PERSON', 'THEFT FROM PERSON - ATTEMPT', 'TILL TAP - GRAND THEFT ($950.01 & OVER)', 'FALSE IMPRISONMENT', 'CONSPIRACY', 'KIDNAPPING - GRAND ATTEMPT', 'SPOUSAL(COHAB) ABUSE - SIMPLE ASSAULT', 'MANSLAUGHTER, NEGLIGENT', 'RAPE, FORCIBLE', 'RESISTING ARREST', 'PICKPOCKET', 'THROWING OBJECT AT MOVING VEHICLE', 'BURGLARY FROM VEHICLE', 'BUNCO, ATTEMPT', 'LYNCHING', 'KIDNAPPING', 'OTHER MISCELLANEOUS CRIME', 'BRANDISH WEAPON', 'SHOPLIFTING-GRAND THEFT ($950.01 & OVER)', 'TRESPASSING', 'ROBBERY', 'EMBEZZLEMENT, GRAND THEFT ($950.01 & OVER)', 'CREDIT CARDS, FRAUD USE ($950.01 & OVER)', 'FALSE POLICE REPORT', 'SEXUAL PENTRATION WITH A FOREIGN OBJECT', 'CONTEMPT OF COURT', 'DRUGS, TO A MINOR', 'INCITING A RIOT', 'VEHICLE - ATTEMPT STOLEN', 'SODOMY/SEXUAL CONTACT B/W PENIS OF ONE PERS TO ANUS OTH', 'DRUNK ROLL - ATTEMPT', 'CHILD ABANDONMENT', 'PURSE SNATCHING', 'THREATENING PHONE CALLS/LETTERS', 'LYNCHING - ATTEMPTED', 'BIKE - STOLEN', 'VANDALISM - MISDEAMEANOR ($399 OR UNDER)', 'BATTERY ON A FIREFIGHTER', 'LEWD CONDUCT', 'CHILD, CRIME AGAINST', 'EMBEZZLEMENT, PETTY THEFT ($950 & UNDER)', 'THEFT FROM MOTOR VEHICLE - ATTEMPT', 'FAILURE TO YIELD', 'STALKING', 'BURGLARY FROM VEHICLE, ATTEMPTED', 'RAPE, ATTEMPTED', 'SHOTS FIRED AT INHABITED DWELLING', 'VIOLATION OF TEMPORARY RESTRAINING ORDER', 'DOCUMENT WORTHLESS ($200 & UNDER)', 'BEASTIALITY, CRIME AGAINST NATURE SEXUAL ASSLT WITH ANIM', 'ATTEMPTED ROBBERY', 'PROWLER', 'BOAT - STOLEN', 'PIMPING', 'ILLEGAL DUMPING', 'PEEPING TOM', 'THEFT, COIN MACHINE - GRAND ($950.01 & OVER)', 'TILL TAP - PETTY ($950 & UNDER)', 'THEFT-GRAND ($950.01 & OVER)EXCPT,GUNS,FOWL,LIVESTK,PROD', 'THEFT PLAIN - PETTY ($950 & UNDER)', 'OTHER ASSAULT', 'THEFT FROM MOTOR VEHICLE - GRAND ($400 AND OVER)', 'DISHONEST EMPLOYEE - PETTY THEFT', 'CONTRIBUTING', 'BOMB SCARE', 'SHOPLIFTING - ATTEMPT', 'RECKLESS DRIVING', 'DRIVING WITHOUT OWNER CONSENT (DWOC)', 'DISRUPT SCHOOL', 'UNAUTHORIZED COMPUTER ACCESS', 'ARSON', 'SHOPLIFTING - PETTY THEFT ($950 & UNDER)', 'ASSAULT WITH DEADLY WEAPON ON POLICE OFFICER', 'THEFT FROM MOTOR VEHICLE - PETTY ($950 & UNDER)', 'PURSE SNATCHING - ATTEMPT', 'CHILD ANNOYING (17YRS & UNDER)', 'DEFRAUDING INNKEEPER/THEFT OF SERVICES, OVER $400', 'PETTY THEFT - AUTO REPAIR', 'WEAPONS POSSESSION/BOMBING', 'CHILD ABUSE (PHYSICAL) - AGGRAVATED ASSAULT', 'DISHONEST EMPLOYEE - GRAND THEFT', 'BATTERY - SIMPLE ASSAULT', 'FAILURE TO DISPERSE',\
        'BIKE - ATTEMPTED STOLEN', 'REPLICA FIREARMS(SALE,DISPLAY,MANUFACTURE OR DISTRIBUTE)', 'ABORTION/ILLEGAL', 'DISHONEST EMPLOYEE ATTEMPTED THEFT', 'INCEST (SEXUAL ACTS BETWEEN BLOOD RELATIVES)', 'BLOCKING DOOR INDUCTION CENTER', 'FIREARMS EMERGENCY PROTECTIVE ORDER (FIREARMS EPO)']
	
    with open('Formatted_Crime_Data_201{}.csv'.format(num)) as orig_file:
        with open('classification_la_data_201{}.csv'.format(num), "wb") as csv_file:
            writer = csv.writer(csv_file, delimiter=',')
            for line in csv.reader(orig_file):
                date_split = line[3].split('/')
                try:
                    crime_index = list_of_crimes.index(line[1])
                except ValueError:
                    print 'ERROR - missing crime: {}'.format(line[1])
                    list_of_crimes.append(line[1])
                    crime_index = list_of_crimes.index(line[1])
                writer.writerow((crime_index,date_split[0],date_split[1],date_split[2],line[4],line[-2],line[-1]))
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

def class_predictor():
    
    X = []
    label = []
    
    for num in range(1): #do loop for each year on LA crime data
        with open('classification_la_data_201{}.csv'.format(num)) as csv_file:
            reader = csv.reader(csv_file)
            for line in reader:
                label.append(float(line[0]))
                X.append([float(item) for item in line[1:]])
            print 'finished processing 201{}'.format(num)
    
    X = np.asarray(X)
    label = np.asarray(label)
        
    
    # print X
    print X.shape
    # print label
    print label.shape
    
    cutoff = X.shape[0]/2
    print cutoff
    
    #set aside test data (1)
    x_train = X[:cutoff]
    x_test = X[cutoff:]
    y_train = label[:cutoff]
    y_test = label[cutoff:]
    
    # print 'x_test:',x_test
    # print 'y_test:',y_test
    
    sgd_clf = SGDClassifier(random_state=42)
    sgd_clf.fit(x_train, y_train)
    print 'training finished'

    print cross_val_score(sgd_clf, x_train, y_train, cv=3, scoring="accuracy")
    # print sgd_clf.predict([x_test])
        
def lead_level_predictor():
    #murder/non-negl. manslaughter, rape, robbery, felony assault, burglary, grand larceny, grand larceny of motor vehicle
    x = [
        [536, 1373, 19486, 16956, 18600, 37835, 10329],
        [515, 1420, 19717, 18482, 18720, 38501, 9314],
        [419, 1445, 20144, 19381, 19168, 42497, 8093],
        [335, 1378, 19128, 20297, 17429, 45368, 7400],
        [333, 1352, 16539, 20207, 16765, 43862, 7664],
        [352, 1438, 16931, 20270, 15125, 44005, 7332]
    ]
    
    y = []
    y.append(0.02252182)
    y.append(0.01877913)
    y.append(0.01358816)
    y.append(0.01204604)
    y.append(0.01093716)
    y.append(0.00877412)
    
    x = np.asarray(x)
    y = np.asarray(y)
    print x.shape
    print y.shape
    
    cutoff = x.shape[0]/2
    print cutoff
    
    x_train = x[:cutoff]
    x_test = x[cutoff:]
    y_train = y[:cutoff]
    y_test = y[cutoff:]
    
    clf = LinearRegression()
    clf.fit(x_train, y_train)
    print 'training finished'
    # print 'predicted val:',clf.predict([x_test])
    # print 'actual val:',y_test
    
    
def gmm():
    X = []
    
    for num in range(1): #do loop for each year on LA crime data
        with open('classification_la_data_201{}.csv'.format(num)) as csv_file:
            reader = csv.reader(csv_file)
            for line in reader:
                X.append([float(item) for item in line])
            print 'finished processing 201{}'.format(num)
    X = np.asarray(X)
    
    # print X
    print X.shape
    # print label
    cutoff = X.shape[0]/2
    print cutoff
    
    #set aside test data (1)
    x_train = X[:cutoff]
    x_test = X[cutoff:]
    
    # shuffle_index = np.random.permutation(cutoff)
    # x_train, y_train = x_train[shuffle_index], y_train[shuffle_index]
    
    # print 'x_test:',x_test
    # print 'y_test:',y_test
    
    gm = GaussianMixture(n_components=2).fit(X)
    print 'training finished'

    labels = gm.predict(X)
    print labels
    # plt.scatter(X[:, 0], X[:, 1], c=labels, s=40, cmap='viridis');
    print len(set(labels))

    
    
    pca = PCA(n_components=2)

    pca.fit(X)

    X_pca = pca.transform(X)
    
    #3d plot
    # shuffle_index = np.random.permutation(cutoff)
    # x_train, B = x_train[shuffle_index], labels[shuffle_index]
    # xs = []
    # ys = []
    # zs = []
    # colors = []
    # color_list = ['b','r','green','cyan','magenta','yellow','black']
    # fig = plt.figure()
    # ax = fig.add_subplot(111, projection='3d')
    # for index,line in enumerate(X_pca[:2000]):
        # if line[2] <= 100:
            # ax.scatter(line[0], line[1], line[2], c=color_list[B[index]])
    # # For each set of style and range settings, plot n random points in the box
    # # defined by x in [23, 32], y in [0, 100], z in [zlow, zhigh].
    # # ax.scatter(xs, ys, zs, colors)
    # ax.set_xlabel('X Label')
    # ax.set_ylabel('Y Label')
    # ax.set_zlabel('Z Label')    
    
    
    # #2d plot of pca
    # shuffle_index = np.random.permutation(cutoff)
    # x_train, B = x_train[shuffle_index], labels[shuffle_index]
    # xs = [line[0] for line in X_pca[:2000]]
    # ys = [line[1] for line in X_pca[:2000]]
    # color_list = ['blue','red','orange','cyan','yellow','green','magenta','black']
    # colors = [color_list[num] for num in B[:2000]]
    # cs = plt.scatter(xs,ys,color=colors)
    
    #plot longitude/latitude
    shuffle_index = np.random.permutation(cutoff)
    x_train, B = x_train[shuffle_index], labels[shuffle_index]
    ys = [line[-2] for line in x_train[:2000]]
    xs = [line[-1] for line in x_train[:2000]]
    color_list = ['blue','green','red','cyan','magenta','yellow','black']
    colors = [color_list[num] for num in B[:2000]]
    cs = plt.scatter(xs,ys,color=colors)

    plt.show()
        
if __name__ == '__main__':
    # for year in range(0,8):
        # process_main_csv(year)
        # process_year_csv(year)
    # class_predictor()
    # lead_level_predictor()
    gmm()