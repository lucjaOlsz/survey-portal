FROM node:18-alpine AS frontend

WORKDIR /frontend
ARG FRONTEND_PATH=src/main/frontend

COPY $FRONTEND_PATH .
RUN npm install && npm run build

FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
COPY --from=frontend /frontend/node_modules /app/src/main/resources/static

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim AS prod
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
