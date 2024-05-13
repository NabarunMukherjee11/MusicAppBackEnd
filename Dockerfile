FROM openjdk:17

WORKDIR /app

COPY speBackend/target/speBackend-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "speBackend-0.0.1-SNAPSHOT.jar"]
