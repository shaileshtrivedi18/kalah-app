Kalah game
---
### Introduction

This is a Java RESTFul web service implementation for Kalah game (6 stones). This web service enables 2 human players to play the game.
Please refer to https://en.wikipedia.org/wiki/Kalah to understand the game and its rules.

### Design / Architecture

Application uses simple controller-service architecture - with persistence layer. In-memory H2 database is used to store the results.

- Java 11
- Spring Boot / MVC
- Lombok
- H2 in-Memory database + JPA
- Swagger documentation
- jUnit + Spring Boot Test

Actual Game execution and request validation logic is present in GameExecutor & Validators.

### Build the application
####Software Requirements:

To build the application, following programs needs to be installed on the machine.
- Java / JDK 11 : https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
- Apache Maven : https://maven.apache.org/download.cgi

Checkout the project from git repository and build it from the project root directory using the command:

`mvn clean install`

### Run the application

`java -jar target/kalah-app-0.0.1-SNAPSHOT.jar`

API documentation (using Swagger) can be checked using URL : `http://localhost:8080/swagger-ui.html`

### Use the application

1. To create a new game, execute the following command:

`curl -X POST -H "Content-Type: application/json" http://localhost:8080/games`

This will create the game with default settings, and save it in the database.

Sample API response:


`{ "id": 1,
   "uri": "http://localhost:8080/games/1"
 }`

2. Once the game is created, then players can start making the moves using the following endpoint:

`curl -X PUT -H "Content-Type: application/json" http://localhost:8080/games/{gameId}/pits/{pitId}`

Sample API response:

`{ "id": 1,
   "uri": "http://localhost:8080/games/1",
   "status": {"1":1,"2":0,"3":8,"4":8,"5":8,"6":8,"7":2,"8":0,"9":8,"10":7,"11":7,"12":7,"13":7,"14":1} 
 }`