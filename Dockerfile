FROM maven:3.8.1-openjdk-17-slim as build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

COPY --from=frontend-build /frontend /app/src/main/resources/static

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim as prod
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
