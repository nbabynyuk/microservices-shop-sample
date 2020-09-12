FROM adoptopenjdk/openjdk11:alpine
ADD target/orders-0.0.1-SNAPSHOT.jar  orders.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "orders.jar"]