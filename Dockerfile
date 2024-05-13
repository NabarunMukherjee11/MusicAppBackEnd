FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk

RUN mkdir /app

WORKDIR /app

COPY speBackend/target/speBackend-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "speBackend-0.0.1-SNAPSHOT.jar"]
