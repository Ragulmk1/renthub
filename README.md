# RentHub - Online Rental  Property Management System
## Project Description
RentHub is a backend application built using Spring Boot (Java)  for an online rental property management system. It provides the necessary APIs and functionalities to manage property listings, tenants, leases, rent payments, and maintenance requests. The system aims to connect property owners, tenants, and property managers efficiently.
## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Configuration](#configuration)
        - [Database Configuration](#database-configuration)
        - [JWT Configuration](#jwt-configuration)
    - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Basic Authentication (Register and Login)](#basic-authentication-register-and-login)
- [Roles and Access Control](#roles-and-access-control)
- [JWT Authorization](#jwt-authorization)
- [Database Initialization](#database-initialization)
- [Contributing](#contributing)
- [License](#license)
## Features
- User registration and authentication
- User roles: Landlord,Tenant
- Role-based access control
- LandLord management
- Tenant management
- JWT-based authentication and authorization
- API Documentation using Swagger
## Technologies Used
- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Hibernate
## Getting Started
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL installed and running
- Maven
### Installation
1. Clone the repository:
   ```bash
   git clone <repository_url>
2. Navigate to the project directory:
   ```bash
   cd RentHub
3. Build the project using Maven:
   ```bash
   mvn clean install
### Configuration
- Locate the application.properties or application.yml file in the src/main/resources directory to configure the application.
### Database Configuration
- Update the database connection properties to match your database setup:
  For application.yml:
```yaml
    spring:
        datasource:
            url: jdbc:mysql://localhost:3306/RentHub?createDatabaseIfNotExist=true
            username: your_database_username
            password: your_database_password
            driver-class-name: com.mysql.cj.jdbc.Driver
```
- spring.datasource.url: The URL of your database. Make sure to include createDatabaseIfNotExist=true to automatically create the database if it doesn't exist. Change the database name (freshbite) if needed.
- spring.datasource.username: Your database username.
- spring.datasource.password: Your database password.
- spring.datasource.driver-class-name:1 The JDBC driver class name for your database.
## JWT Configuration
- In the same application.yml file, configure the JWT properties:
```yaml
   jwt:
      secret: your_jwt_secret_key # Change this to a strong, random string
      expiration: 86400000    # Token expiration time in milliseconds (e.g., 24 hours)
```
- jwt.secret: A secret key used to sign the JWT. This should be a long, random, and secure string. Do not use a simple or predictable value.
- jwt.expiration: The time in milliseconds until the JWT expires. The example value 86400000 is 24 hours (24 * 60 *
  60 * 1000).
### Running the Application
## Using mvn
1. Navigate to the project root directory in your terminal.
2. Run the Spring Boot application using Maven:
   ```bash
        mvn spring-boot:run
The application will start, and you should see log messages indicating that it is running. By default, Spring Boot
applications run on port 8080.
## Using IntelliJ
1. Create a Configuration using IntelliJ Interface and set profile as dev using below environment variable
   ```editorconfig
### API Documentation
- The API documentation can be accessed via Swagger UI at the following URL:
- [Swagger Interface](#http://localhost:8080/swagger-ui/)
### Basic Authentication (Register and Login)
The application provides the following endpoints for basic authentication:
1. Register: /register - Allows new users to register with their credentials (username, password, etc.) and role.
2. Login: /login - Authenticates users and returns a JWT upon successful login.
### Roles and Access Control
The application uses the following roles:
1. Landlord: To upload properties .
2. Tenant: To rent properties .
Access to specific controllers and endpoints is restricted based on these roles.
If a user tries to access a resource they are not authorized to access, the server will return a 403 Forbidden error.
### JWT Authorization
- After successful login, the server returns a JWT. This token must be included in the Authorization header of
  subsequent requests to access protected resources.
To authorize your requests in Swagger:
Open the Swagger UI.
1. Click the "Authorize" button.
2. In the dialog box, enter the JWT in the following format:
```text
   your_jwt_token
```
Click "Authorize" and then "Close".
- Now, Swagger will include the Authorization header with your JWT for all requests, allowing you to access protected
  endpoints.
### Database Initialization
- The database will be automatically created if it does not exist, as configured in the  
  application.yml file (createDatabaseIfNotExist=true).