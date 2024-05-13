FROM ubuntu:latest

# Install necessary dependencies
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk

RUN mkdir /app

COPY speBackend/target/speBackend-0.0.1-SNAPSHOT.jar /app

WORKDIR /app

EXPOSE 9292

CMD ["java", "-jar", "speBackend-0.0.1-SNAPSHOT.jar"]
