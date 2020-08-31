FROM adoptopenjdk/openjdk11:alpine
ADD target/product-catalog.jar  product-catalog.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "product-catalog.jar", "generate-dummy-data"]
