FROM adoptopenjdk/openjdk11:alpine
ADD target/userapp.jar  userapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "userapp.jar"]