--Yixin Chen
--Final Project "SQL Saturday"
--COMP440 Spring 2017 Monday Session
--Create Complete Presentation View Script

USE s17Guest01;
GO

IF ( OBJECT_ID('PresentationView') IS NOT NULL ) 
	BEGIN
	DROP VIEW PresentationView;
	END
GO

CREATE VIEW PresentationView
  AS
  SELECT Presentation.ID, Presentation.Title, Person.FirstName, Person.LastName, Presentation.Difficulty, Track.[Name] AS Track, [Event].City AS [Event]
  FROM Presentation 
  INNER JOIN [Event] ON Presentation.EventID = [Event].ID
  INNER JOIN PresentationTrack ON Presentation.ID = PresentationTrack.PresentationID
  INNER JOIN Track ON PresentationTrack.TrackID = Track.ID
  INNER JOIN PresentationPerson ON Presentation.ID = PresentationPerson.PresentationID
  INNER JOIN Person ON Person.ID = PresentationPerson.PersonID;