FROM arm32v7/openjdk:11.0.1-jdk-slim

EXPOSE 8080

ADD target/security-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar" ]
