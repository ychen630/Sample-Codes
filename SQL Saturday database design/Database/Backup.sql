--Yixin Chen
--Final Project "SQL Saturday"
--COMP440 Spring 2017 Monday Session
--BACKUP Script

USE s17guest01;
GO

BACKUP DATABASE s17guest01
    TO DISK = '\s17guest01.Bak'
      WITH FORMAT,
        MEDIANAME = 'Z_SQLServerBackups',
        NAME = 'Full Backup of s17guest01';
GO