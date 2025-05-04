# News Aggregator - News Aggregator Management API

## Overview

This Spring Boot application enables users to register and manage their accounts, configure personalized news preferences across various categories, and seamlessly access the latest articles aggregated from external news APIs. It also supports marking articles as favorites, caching for faster retrieval, and secure user authentication using JWT tokens.

## Features

- **User Registration**: Create a new user account.
- **User Login**: Authenticate user and generate a JWT Token.
- **News Prefences**: Users can select preferred news categories.
- **View News**: Fetches news articles based on user preferences.
- **Favorite News**: Mark articles as favorite for quick access.
- **Cache News Articles**: Improve perfomance by caching news articles.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security (JWT Authentication)**
- **H2 Database(in - memory)**
- **Spring Validation**
- **Caffeine Cache**
- **Lombok**
- **SLF4J (Logging)**
- **Maven** (for dependency management)


## Note on Data Persistence 

- User information, preferences, and favorite news articles are stored in an H2 in-memory database.

- External news articles are fetched from a third-party News API and optionally cached for better performance.


## Project Structure

```bash
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── newsaggregaotr
│   │               ├── controller      # Contains REST controllers 
│   │               ├── config          # Contains Congigurations(Beans)
│   │               ├── cache           # Contains Cache Configurations
│   │               ├── entity          # Contains User
│   │               ├── exception       # Contains Centralized Exception Handling
│   │               ├── jwt             # Contains JWT Based Configurations
│   │               ├── login           # Contains Registration and Login Classes
│   │               ├── model           # Contains news Related classes
│   │               ├── repo            # Contains Repository Information
│   │               ├── response        # Contains API Response
│   │               ├── service         # Contains Service Implementation
│   └── resources
│       └── application.properties      # Application configuration
├── README.md
├── pom.xml

```
## **API Endpoints**
```bash

| Method | Endpoint                          | Description                            |
| :----: | :-------------------------------: | :-------------------------------------:|
|  POST  | /api/user/register                | Register a new user                    |
|  POST  | /api/user/login                   | Authenticate user and get JWT token    |
|  GET   | /api/news/preferences             | Get news preferences                   |
|  PUT   | /api/news/update-preferences      | Update preferences for the user        |
|  POST  | /api/news/search/{keyword}        | Mark a news article as favorite        |
|   GET  | /api/news/{id}/read               | View all favorite news articles        |
|   GET  | /api/news/{id}/favorite           | View all favorite news articles        |
|   GET  | /api/news/read                    | View all favorite news articles        |
|   GET  | /api/news/favorites               | View all favorite news articles        |

```

## Running the Application
- **Prerequisites**
    - Java 17+
    - Maven 3.8+

- **The application will start at** :- http://localhost:9090


- **Steps**
    - **GIT Clone** :- https://github.com/airtribe-projects/news-aggregator-api-with-spring-boot-PVVCK.git
    - cd news-aggregator
    - mvn spring-boot:run


- **H2 Console:** http://localhost:9090/h2-console (username/password configured in application.properties)