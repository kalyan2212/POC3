FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/diagram-parser-service-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]