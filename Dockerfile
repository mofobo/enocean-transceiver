FROM arm64v8/openjdk:21-jdk-slim
WORKDIR /app
COPY target/enocean-transceiver-1.0-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "./app.jar"]
LABEL authors="mofobo"