FROM maven:3.9.6-eclipse-temurin-8 AS builder
LABEL authors="atom"
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:8.402.06-r0
COPY --from=builder target/spring-data-jpa-demo-*.jar /app/app.jar
ENTRYPOINT ["java","-jar","-Dspring.config.location=/app/config/application.yml","/app/app.jar"]
EXPOSE 8080
