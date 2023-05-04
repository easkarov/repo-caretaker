FROM openjdk:17

COPY target/scrapper-1.0-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]