FROM eclipse-temurin:21-jdk
COPY target/TestProject-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]