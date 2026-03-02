FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/inventory-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]