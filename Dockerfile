FROM openjdk17:alpine

WORKDIR /app

COPY speBackend/target/speBackend-0.0.1-SNAPSHOT.jar /app/speBackend.jar

EXPOSE 9292

CMD ["java", "-jar", "speBackend.jar"]
