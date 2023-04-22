[comment]: <> (# Highway Emergency Location Platform )
<div align="center" spacing="5">
<a href="https://imgur.com/o56PeaZ"><img src="https://i.imgur.com/o56PeaZ.png" title="source: imgur.com" /></a>
</div>
<div align="center">
Highway Emergency Location Platform
</div>

# Goals
### The Problem
In most cities, people rely on their "go-to" towing service provided through insurance companies or search engines like Google. This limits competition in the market and leaves consumers with few options. New or struggling towing companies struggle to establish themselves in the market due to a lack of visibility and resources.

### The solution
We propose an application that will support the towing industry by providing a more efficient and user-friendly way for consumers to request tow services. Our app will function similarly to popular ride-hailing platforms like Uber and Lyft, offering a semi-anonymous discovery process for tow companies that are registered on our platform.

Our app will not only benefit consumers by providing them with a convenient and reliable way to request tow services, but it will also benefit tow companies by giving them increased visibility and access to a wider pool of potential customers. This will help to level the playing field and create more competition in the market, ultimately leading to better services and pricing for consumers.

### Problems
Of course, through the development of this project, there have been numerous problems which are currently still being tackled.
This project was handed over to me (Michael Rasch) and I would like to address some shortcomings which should be ironed out.
- The police department implementation has been stopped due to lack of scope/requirements which failed to specify the direct functionality they provide.
  - Originally, the police officer was the person who was in charge of calling the tow truck service for the end user (the driver in need of the tow). However, this implementation needs a detailed plan as the client previously wanted the dispatch center to issue the request through the police officer. As just a developer, this portion seems inadequate to implement.
- For the last sprint, I migrated to Spring JPA. This could have caused some unknown issues. In testing, I did not find any, but still be aware.
## Pre Requisites
- Experience with RESTApis
- Experience with relational databases.
- Experience with Spring Boot/Security/JPA
- Postman or your desired API testing solution.

## Set Up
- A locally hosted PostgresSQL database.
- Clone the project into your respected IDE.
- From the IDE, take a look at the applications.properties folder, these are the environmental variables which need to be filled out before running. Please pay special attention to the {FILL IN} blanks, which you will need to manually specify.
  ``` 
  LOGGING_LEVEL_WEB=DEBUG;LOGGING_LEVEL_SQL=DEBUG;PROFILE=dev;PORT=3000;SERVER_ADDRESS={FILL IN};
  SPRING_DATASOURCE_URL={FILL IN};SPRING_DATASOURCE_USERNAME={FILL IN};
  SPRING_DATASOURCE_PASSWORD={FILL IN};FLYWAY_ENABLED=true;FLYWAY_SCHEMA=public;FLYWAY_BASELINE=false;
  FLYWAY_CLEAN_ON_ERROR=true;FLYWAY_VALIDATE_ON_MIGRATE=true;MAIL_PROTOCOL=smtp;MAIL_HOST=smtp.gmail.com;
  MAIL_PORT=587;MAIL_EMAIL={FILL IN};MAIL_APP_PASSWORD={FILL IN};MAIL_SMTP_AUTH=true;
  MAIL_START_TTLS=true;PRETTY_PRINT=true;JWT_SECRET={FILL IN};JWT_EXPIRATION=604800000;
  MAX_UPLOAD_SIZE_KB=700KB;MAX_REQUEST_SIZE_KB=700KB;CUSTOMER_SUPPORT_EMAIL="{FILL IN}";
  SPRING_DNS=0.0.0.0.0;INCLUDE_STACKTRACE=never;MAX_FILE_THRESHOLD=128KB
  ```
- Click the big green button to run the application.
- Hopefully it worked

## Common Set Up Problems
- PSQLException
  - Typically, means your locally hosted database is not correctly setup/credentials filled. Important to note, the test cases rely on a separate database to be hosted.
- IllegalStateException
  - Double-check the environmental variables are properly set. In this case, the error is throwing because a file expects the @Value annotation to be correctly filled.

## Contact
For any questions, or help with understanding what is happening feel free to email me: raschmichael10@gmail.com or just make a pull request.

## Technologies Used
  - Spring Boot/JPA/Security/Websockets/Mail
  - Java JWT
  - Lombok
  - Postgres
## Resources
[Postman Tutorial](https://www.youtube.com/watch?v=cGn_LTFCif0&list=PLhW3qG5bs-L9P22XSnRe4suiWL4acXG-g)

[Spring Tutorial V1](https://www.baeldung.com/)

[Spring Tutorial V2](https://spring.io/guides)

[Spring In Action (Awesome Book)](https://www.amazon.com/Spring-Action-Sixth-Craig-Walls/dp/1617297577/ref=sr_1_1?keywords=Spring+in+action&qid=1681099073&sr=8-1)

[Postgres Setup](https://www.youtube.com/watch?v=BLH3s5eTL4Y)

[RestAPI Tutorial](https://www.youtube.com/watch?v=7YcW25PHnAA)



