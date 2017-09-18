--Yixin Chen
--Final Project "SQL Saturday"
--COMP440 Spring 2017 Monday Session
--Insert Presentations Script 
--Required Procedure

USE s17Guest01;
GO


IF ( OBJECT_ID('insertPresentation') IS NOT NULL ) 
	BEGIN
	DROP PROCEDURE insertPresentation;
	END
GO

CREATE PROCEDURE insertPresentation @Speaker VARCHAR(100), @Title VARCHAR(255)
	AS
	BEGIN
		DECLARE @FirstName VARCHAR(50);
		DECLARE @LastName VARCHAR(50);
		DECLARE	@Name VARCHAR(100);
		DECLARE @PersonID INT;
		DECLARE @Position INT = 1;
		DECLARE @Space INT = 0;
		
		SET @Name = (SELECT RTRIM(@Speaker));

		--check if Speaker string has a space
		WHILE(@Position < LEN(@Name) AND @Space = 0)
			BEGIN
				IF(SUBSTRING(@Name, @Position, 1) = ' ')
					BEGIN
						SET @Space = 1;
					END
				SET @Position = @Position + 1;
			END

		--If space found, seperate the name to first name and last name
		IF(@Space = 1)
			BEGIN
				SET @FirstName = SUBSTRING(@Name, 1, CHARINDEX(' ', @Name) - 1);
				SET @LastName = SUBSTRING(@Name, CHARINDEX(' ', @Name) + 1, LEN(@Name) - CHARINDEX(' ', @Name));
		
				IF NOT EXISTS (SELECT * FROM Person WHERE FirstName = @FirstName AND LastName = @LastName)
					BEGIN
						--insert name
						INSERT INTO Person(FirstName,LastName) VALUES (@FirstName,@LastName);
						SET @PersonID = (SELECT TOP 1 ID FROM Person WHERE FirstName = @FirstName AND LastName = @LastName);
						--insert person role "presenter"
						INSERT INTO PersonRole(PersonID, RoleID) VALUES (@PersonID, (SELECT ID FROM Roles WHERE [Role]='Presenter'));
					 END
			END
		--No space found, treat the name as first
		ELSE
			BEGIN
				SET @FirstName = @Name
				IF NOT EXISTS (SELECT * FROM Person WHERE FirstName = @FirstName AND LastName IS NULL)
					BEGIN
						--insert name
						INSERT INTO Person(FirstName) VALUES (@FirstName);
						SET @PersonID = (SELECT TOP 1 ID FROM Person WHERE FirstName = @FirstName AND LastName IS NULL);
						--insert person role "presenter"
						INSERT INTO PersonRole(PersonID, RoleID) VALUES (@PersonID, (SELECT ID FROM Roles WHERE [Role]='Presenter'));
					 END
			END
		
		--get person ID from Person table
		SET @PersonID = (SELECT TOP 1 ID FROM Person WHERE FirstName = @FirstName AND LastName = @LastName);

		--Insert presentation title into Presentation table
		INSERT INTO Presentation(Title) VALUES (@Title);

		--Insert presentation ID and Speaker ID into PersentationPerson table
		INSERT INTO PresentationPerson(PresentationID, PersonID) VALUES ((SELECT TOP 1 ID FROM Presentation WHERE Title = @Title),@personID);
		
	END
GO


EXEC insertPresentation 'Jack Alan', 'Final report of COMP 484';