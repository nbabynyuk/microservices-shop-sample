FROM adoptopenjdk/openjdk11:alpine
ADD target/DiscoveryServer-0.0.1-SNAPSHOT.jar DiscoveryServer.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "DiscoveryServer.jar"]
