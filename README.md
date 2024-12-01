# Spring Web App

This project is a Spring web application that uses Thymeleaf, Tailwind CSS, Flowbite, Spring Boot, PostgreSQL, and Docker.

## Prerequisites

- Docker
- Docker Compose
- Java
- Maven
- Npm

## Getting Started

1. **Copy Environment Variables**

   Copy the environment variables from `.env.init` to `.env` and fill in the required values.

   ```shell
   cp .env.init .env
   ```

2. **Build the containers using Docker Compose**

   1. webapp - Spring Boot application
   2. db - PostgreSQL database
   3. webserver - Nginx web server
      
   ```shell
   docker-compose up --build
   ```
3. **Access the application**

    Access the application at [http://localhost](http://localhost)
4. **Live Reload**
    Set in `.env` file under MAVEN_OPTS the following value:
    ```dotenv
    MAVEN_OPTS="-Dspring.devtools.restart.enabled=true -Dspring.devtools.livereload.enabled=true"
    ```
    To enable live reload, set in your IDE auto build after changes in the project. The application will be reloaded automatically.
    Set on port 35729 in your IDE.
