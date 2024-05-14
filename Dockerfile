FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copy the Maven project and build the application
COPY speBackend /app
RUN mvn -f /app clean package

FROM openjdk:17-slim

WORKDIR /app

# Copy the JAR file from the build stage to the new image
COPY --from=build /app/target/speBackend-0.0.1-SNAPSHOT.jar /app/speBackend-0.0.1-SNAPSHOT.jar

EXPOSE 9292

CMD ["java", "-jar", "speBackend-0.0.1-SNAPSHOT.jar"]
