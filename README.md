<!--
*** Thanks for checking out Spring Boot Application Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again!
-->

[comment]: <> (# Highway Emergency Location Platform )
<div align="center" spacing="5">
<a href="https://imgur.com/o56PeaZ"><img src="https://i.imgur.com/o56PeaZ.png" title="source: imgur.com" /></a>
</div>
<div align="center">
Highway Emergency Location Platform

</div>


<div align="center">

<br>

<div align="center">
  <sub>Built with ❤︎ by <a href="https://github.com/kayleqb">Quincy Kayle</a>  <a href="https://github.com/MattRank93">Matthew Rank</a> and <a href="https://github.com/mikerasch">Michael Rasch</a>
</div>



<p align="center">
	<a href="https://documenter.getpostman.com/view/13688383/TVmLCyby#61200d8c-9d42-4cb3-9dc7-b96fa7dca8c2"><strong>Explore the Postman example docs »</strong></a>
	<br />
	<br />

</p>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
-->

## Important Note: This app in not yet complete, expect MVP soon

## Technology stack & other Open-source libraries

### Database

<details open="open">
   <ul>
      <li><a href="https://flywaydb.org/">Flyway</a> - Version control for database</li>
      <li><a href="https://www.postgresql.org//">postgresql</a> - Open-Source Relational Database Management System</li>
      <li><a href="https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html">JDBC</a> - Java SQL database. Embedded and server modes; in-memory databases</li>
   </ul>
</details>

### Server - Backend

<details open="open">
   <ul>
      <li><a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">JDK</a> - Java™ Platform, Standard Edition Development Kit</li>
      <li><a href="https://spring.io/projects/spring-boot">Spring Boot</a> - Framework to ease the bootstrapping and development of new Spring Applications</li>
      <li><a href="https://gradle.org/">Gradle</a> - Dependency Management</li>
      <li><a href="https://www.jsonwebtoken.io/">JSON Web Token</a> - Encode or Decode JWTs</li>
      <li><a href="http://tomcat.apache.org/">Tomcat</a> - java servlet technology</li>
      <li><a href="https://projectlombok.org/">Lombok</a> - Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.</li>

   </ul>
</details>



### Others

<details open="open">
   <ul>
      <li><a href="https://git-scm.com/">git</a> - Free and Open-Source distributed version control system</li>
      <li><a href="https://visualvm.github.io/">VisualVM</a> - Monitoring system and time series database</li>
   </ul>
</details>

### External Tools & Services

<details open="open">
   <ul>
      <li><a href="https://www.getpostman.com/">Postman</a> - API Development Environment (Testing Docmentation)</li>
      <li><a href="https://docs.postman-echo.com/?version=latest">Postman Echo</a> - A service that can be used to test your REST clients and make sample API calls. It provides endpoints for GET, POST, PUT, various auth mechanisms and other utility endpoints.</li>
      <li><a href="https://www.toptal.com/developers/gitignore/api/java,eclipse,intellij">gitignore.io</a> - Create useful .gitignore files for your project.</li>
   </ul>
</details>

## Features and To-Do

<details open="open">
   <ul>
      <li>[x] <a href="https://spring.io/projects/spring-security">Spring Security</a> RBAC, Session Timeout</li>
      <li>[x] API <a href="https://en.wikipedia.org/wiki/Rate_limiting">Rate Limiting</a></li>
      <li>[x] <a href="https://www.docker.com/">Docker</a></li>
      <li>[x] <a href="https://en.wikipedia.org/wiki/HTTPS">HTTPS</a> with <a href="https://en.wikipedia.org/wiki/Self-signed_certificate">(self-signed certificate)</a></li>
      <li>[ ] Multiple Databases</li>
      <li>[ ] Unit Tests, Integration Tests</li>
      <li>[ ] SCreate jenkins pipeline </li>
      <li>[ ] Spring Boot supreme Admin</li>
      <li>[ ] Spring Retry</li>
   </ul>
</details>

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* To activate the accounts of registered users, an email with activation link is sent to the email provided during the
  user signup stage.

  [Mailtrap](https://mailtrap.io/) or any other service like **Gmail**, etc., can be used to create an SMTP.

  update the **springMailHost**, **springMailPort**, **springMailProtocol**, **springMailUsername** and **
  springMailPassword** details in the **application_settings**

* You need to have **Postgres** installed on your machine to run the application in **`dev`** profile. Using
  the postgres client/console, create a database/schema named `tow`.

~~~sql
-- create schema I NEED TO CHANGE THIS STUFF
CREATE SCHEMA tow;

-- use schema
USE
sbat;

-- Create user 
create
user 'user'@'localhost' identified by 'usert';

-- Grant privileges to user
grant all privileges on *.* to
'user'@'localhost' with grant option;
~~~

After creating the database/schema, you need to add your **Postgres** `username` and `password` in
the `application-dev.properties` file on `src/main/resource`. The lines that must be modified are as follows: Then add the appropriate 
information to the environment variables in the IDE

```properties
# ===============================
# = DATA SOURCE - PostgreSQL
# ===============================
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.drivers-class-name=org.postgresql.Driver
```

### EER Diagram

* Refer to [ARCHITECTURE.md](documents/ARCHITECTURE.md) for details.

## Installing

* URL to access application routes: **http://localhost:3007/api/**



#### Running the application with IDE

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
in the `com.arc.sbtest.SBtemplateApplication` class from your IDE.

* Download the zip or clone the Git repository.
* Unzip the zip file (if you downloaded one)
* Open Command Prompt and Change directory (cd) to folder containing pom.xml
* Open Eclipse
    * File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
    * Select the project
* Choose the Spring Boot Application file (search for @SpringBootApplication)
* Right Click on the file and Run as Java Application



#### Running the application with Executable JAR

The code can also be built into a jar and then executed/run. Once the jar is built, run the jar by double clicking on it
or by using the command

```shell
$ git clone https://github.com/uwp-se/TOW-Server.git
$ cd Spring-Boot-help-app
$ gradle package -DskipTests
$ java -jar <filename>.jar --spring.profiles.active=test
```


#### Running the application via docker container

* Will hopefully have good docker image ready by end of semester.


## Testing API

### Testing with Postman

Refer to link at top of page for postman testing link


