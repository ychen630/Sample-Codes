--Yixin Chen
--Final Project "SQL Saturday"
--COMP440 Spring 2017 Monday Session
--Budapest Presentations Procedure Script
--Required procedure

USE s17Guest01;
GO


IF ( OBJECT_ID('BudapestPresentations') IS NOT NULL ) 
	BEGIN
	DROP PROCEDURE BudapestPresentations;
	END
GO

CREATE PROCEDURE BudapestPresentations 
	AS
	BEGIN
		SELECT * FROM PresentationView WHERE [Event] = 'Budapest' ORDER BY Track;
	END
GO

EXEC BudapestPresentations 

