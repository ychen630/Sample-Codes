--Yixin Chen
--Final Project "SQL Saturday"
--COMP440 Spring 2017 Monday Session
--Initial Insert Presentation (Sample data) Procedure Script

USE s17Guest01;
GO

IF ( OBJECT_ID('InsertPresentationInitial') IS NOT NULL ) 
	BEGIN
	DROP PROCEDURE InsertPresentationInitial;
	END
GO

CREATE PROCEDURE InsertPresentationInitial @Title VARCHAR(255), @Speaker VARCHAR(100), @DifficultyLevel VARCHAR(50), @Event VARCHAR(50), @Track VARCHAR(50)
	AS
	BEGIN
		DECLARE @FirstName VARCHAR(50);
		DECLARE @LastName VARCHAR(50);
		DECLARE	@Name VARCHAR(100);
		DECLARE @PersonID INT;
		DECLARE	@EventID INT;
		DECLARE @TrackID INT;
		
		SET @Name = @Speaker;
		SET @FirstName = SUBSTRING(@Name, 1, CHARINDEX(' ', @Name) - 1);
		SET @LastName = SUBSTRING(@Name, CHARINDEX(' ', @Name) + 1, LEN(@Name) - CHARINDEX(' ', @Name));
		
		IF NOT EXISTS (SELECT * FROM Person WHERE FirstName = @FirstName AND LastName = @LastName)
			BEGIN
				INSERT INTO Person(FirstName,LastName) VALUES (@FirstName,@LastName);
				SET @PersonID = (SELECT TOP 1 ID FROM Person WHERE FirstName = @FirstName AND LastName = @LastName);
				INSERT INTO PersonRole(PersonID, RoleID) VALUES (@PersonID, (SELECT ID FROM Roles WHERE [Role]='Presenter'));
        END
		
		SET @PersonID = (SELECT TOP 1 ID FROM Person WHERE FirstName = @FirstName AND LastName = @LastName);
		SET @EventID = (SElECT TOP 1 ID FROM [Event] WHERE City = @Event);
		SET @TrackID = (SElECT TOP 1 ID FROM [Track] WHERE [Name] = @Track);

		INSERT INTO Presentation(Title, Difficulty, EventID) VALUES (@Title, @DifficultyLevel, @EventID);
		INSERT INTO PresentationPerson(PresentationID, PersonID) VALUES ((SELECT TOP 1 ID FROM Presentation WHERE Title = @Title), @PersonID);
		INSERT INTO PresentationTrack(PresentationID, TrackID) VALUES ((SELECT TOP 1 ID FROM Presentation WHERE Title = @Title), @TrackID)
		
	END
GO


