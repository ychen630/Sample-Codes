--Yixin Chen
--Final Project "SQL Saturday"
--COMP440 Spring 2017 Monday Session
--Create Tables Script

USE s17Guest01;
GO

--Create Person table, which stores information of a person
CREATE TABLE Person(
	ID INT IDENTITY  PRIMARY KEY NOT NULL,
	FirstName VARCHAR(50) NOT NULL,
	LastName VARCHAR(50) NOT NULL,
	Street VARCHAR(255),
	City VARCHAR(50),
	[State] VARCHAR(50),
	ZipCode VARCHAR(50),
	Email VARCHAR(50)
);

--Create Roles table, which stores the name of roles, including
CREATE TABLE Roles(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	[Role] VARCHAR(50) NOT NULL UNIQUE
);

--Create cross reference table between person and roles
CREATE TABLE PersonRole(
	PersonID INT NOT NULL FOREIGN KEY REFERENCES Person(ID),
	RoleID INT NOT NULL  FOREIGN KEY REFERENCES Roles(ID),
	UNIQUE (PersonID, RoleID)
);

--Create Event table storing ID, Date, Name, holding city and region
CREATE TABLE [Event](
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	[Date] DATE NOT NULL,
	[Name] VARCHAR(100) NOT NULL,
	City VARCHAR(50) NOT NULL,
	Region VARCHAR(100) NOT NULL,
	Address VARCHAR(255)
);

--Create Presentation table, which stores information of Presentation, include ID, Title, Description, and difficulty level
CREATE TABLE Presentation(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	Title VARCHAR(255) NOT NULL,
	[Description] VARCHAR(255),
	Difficulty VARCHAR(50),
	EventID INT FOREIGN KEY REFERENCES [Event](ID),
	UNIQUE(ID, EventID)
);

--Create Track table, storing ID and Track name
CREATE TABLE Track(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	[Name] VARCHAR(100) NOT NULL
);

--Create cross reference table between Presentation and Track 
CREATE TABLE PresentationTrack(
	PresentationID INT NOT NULL FOREIGN KEY REFERENCES Presentation(ID),
	TrackID INT FOREIGN KEY REFERENCES Track(ID),
	UNIQUE (PresentationID, TrackID)
);

--Create cross reference table between Presentation and Person
CREATE TABLE PresentationPerson(
	PresentationID INT NOT NULL FOREIGN KEY REFERENCES Presentation(ID),
	PersonID INT NOT NULL FOREIGN KEY REFERENCES Person(ID),
	UNIQUE (PresentationID, PersonID)
);


--Create Room table storing ID, Room Name and Capacity
CREATE TABLE Room(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	Room VARCHAR(50) NOT NULL UNIQUE,
	Capacity INT NOT NULL
);

--Create TimeSlot table, storing ID, start time and end time 
CREATE TABLE TimeSlot(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	StartTime TIME NOT NULL,
	EndTime TIME NOT NULL,
	UNIQUE (StartTime, EndTime)
);

--Create Schedule table with EventID, presentations, room, time slot
CREATE TABLE Schedule(
	EventID INT NOT NULL FOREIGN KEY REFERENCES Event(ID),
	PresentationID INT NOT NULL FOREIGN KEY REFERENCES Presentation(ID),
	RoomID INT NOT NULL FOREIGN KEY REFERENCES Room(ID),
	TimeSlotID INT NOT NULL FOREIGN KEY REFERENCES TimeSlot(ID),
	UNIQUE(EventID, PresentationID, RoomID, TimeSlotID)
);


--Create sponsor table wiht sponsor ID, Name and sponsor level
CREATE TABLE Sponsor(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	[Name] VARCHAR(50) NOT NULL,
	[Level] VARCHAR(50) NOT NULL
);

--Create cross reference table between Sponsor and Person
CREATE TABLE SponsorPerson(
	SponsorID INT NOT NULL FOREIGN KEY REFERENCES Sponsor(ID),
	PersonID INT NOT NULL FOREIGN KEY REFERENCES Person(ID),
	UNIQUE (SponsorID, PersonID)
);

--Create EventTable table, storing table ID, the # of table, eventID
CREATE TABLE EventTable(
	ID INT IDENTITY PRIMARY KEY NOT NULL,
	[Number] INT NOT NULL,
	EventID INT NOT NULL FOREIGN KEY REFERENCES Event(ID),
	CHECK ([Number] <= 10),
	UNIQUE(EventID, [Number])
);

--Create cross reference table between Person and EventTable
CREATE TABLE EventTablePerson(
	PersonID INT NOT NULL FOREIGN KEY REFERENCES Person(ID),
	TableID INT NOT NULL FOREIGN KEY REFERENCES EventTable(ID),
	UNIQUE (PersonID, TableID)
);

--Create GiftRaffle table with Raffle ID, eventID and PersonID
CREATE TABLE GiftRaffle(
	ID INT NOT NULL,
	EventID INT NOT NULL FOREIGN KEY REFERENCES Event(ID),
	PersonID INT NOT NULL FOREIGN KEY REFERENCES Person(ID),
	UNIQUE(ID, EventID, PersonID)
);
