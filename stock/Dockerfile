FROM adoptopenjdk/openjdk11:alpine
ADD target/stock-0.0.1-SNAPSHOT.jar  stock.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "stock.jar"]