FROM openjdk:17

COPY target/scrapper-1.0-SNAPSHOT.jar /scrapper.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "scrapper.jar"]
