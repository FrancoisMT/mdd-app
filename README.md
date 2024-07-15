# mdd-app
Blog allowing users to subscribe/unsubscribe to topics, view all articles associated with the topics to which they are subscribed, create articles, and post comments.

## Setting up the Database
Verify the presence of a MySQL database instance installed and set up correctly on your system
The SQL script responsible for inserting initialias datas is located in the folder: src/main/ressources/scripts/database/data.sql

### Environment Variables for Database
To run this project, you must configure the following environment variables:
- `DATASOURCE_URL`: The database connection URL.   (ex : jdbc:mysql://localhost:3306/{yoga-db}?allowPublicKeyRetrieval=true
- `DATASOURCE_USERNAME`: The username for the database.  (ex: root)
- `DATASOURCE_PASSWORD`: The password for the database.  (ex : my-db-pwd)

## Installing the Application
1. Make sure you've installed all needed dependencies: Java, Node.js, Maven.
2. Clone this repo to your local environment.
3. Move to the back-end project folder and execute `mvn clean install` to fetch dependencies and build the project.
4. Go to the front-end project folder and use `npm install` to fetch front-end dependencies.

## Launching the Application
1. In the back-end project folder, run `mvn spring-boot:run` to initiate the back-end server.
2. In the front-end project folder, use `ng serve` to start the front-end interface.

## Executing Tests

### Front-end Unit and Integration Testing (Jest)
1. In the front-end project folder, execute `npm run test` to conduct front-end unit tests via Jest.
### Back-end Unit and Integration Testing (JUnit and Mockito)
1. In the back-end project area, run `mvn clean test` to perform back-end unit and integration tests via JUnit and Mockito

## Conducting tests using Swagger UI
- Run the application mdd-app
- Access the Swagger UI documentation at http://localhost:8080/swagger-ui/index.html

## Main technologies used in the development of the mdd App :
- NodeJS v20
- Angular CLI v17
- Java 21
- SpringBoot 3.3.0
- JUnit
- Mockito
- Jest
