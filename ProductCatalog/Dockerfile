FROM openjdk:8-jdk-alpine
ADD target/product-catalog.jar  product-catalog.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "userapp.jar", "generate-dummy-data"]