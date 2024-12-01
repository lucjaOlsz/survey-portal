FROM maven:3.8.1-openjdk-17-slim as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
