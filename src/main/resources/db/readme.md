# Migations Readme

## Make sure Gradle is installed on your machine

1. Visit - https://gradle.org/install/
2. Install with Homebrew
    1. How to Install Homebrew (Mac OS)
        * /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    2. Install Gradle with Homebrew
        * brew install gradle
        
## To Run the Migrations

* gradle build flywayMigrate -i (This will run the migrations)
* gradle build flywayClean -i (This will clean the DB)

## Structuring your URL

* jdbc:postgresql:database
* jdbc:postgresql:/
* jdbc:postgresql://host/database
* jdbc:postgresql://host/
* jdbc:postgresql://host:port/database
* jdbc:postgresql://host:port/

## Migration Versions

### V1_Create_Initial_Tables

This script version will create all of the tables initially needed in phase 1 for the DB
