FROM adoptopenjdk/openjdk11:alpine
ADD target/payments-0.0.1-SNAPSHOT.jar  payments.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "payments.jar"]