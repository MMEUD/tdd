
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, and Azure
-- --------------------------------------------------
-- Date Created: 05/15/2013 20:07:50
-- Generated from EDMX file: G:\MoodWeatherFinal\MoodWeatherFinal\MoodWeather01.Web\SqlModel\WeatherRaports.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [CityList];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------


-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[CityStatusViewStoreContainer].[viewCityStatus]', 'U') IS NOT NULL
    DROP TABLE [CityStatusViewStoreContainer].[viewCityStatus];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'viewCityStatus'
CREATE TABLE [dbo].[viewCityStatus] (
    [Id] int  NOT NULL,
    [CityName] varchar(250)  NULL,
    [CityAlternative] varchar(250)  NULL,
    [CountryCode] nvarchar(3)  NULL,
    [Teamco] nvarchar(3)  NULL,
    [Country] nvarchar(50)  NULL,
    [LastUpdate] datetime  NULL,
    [Active] tinyint  NULL,
    [CityId] int  NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [Id] in table 'viewCityStatus'
ALTER TABLE [dbo].[viewCityStatus]
ADD CONSTRAINT [PK_viewCityStatus]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------