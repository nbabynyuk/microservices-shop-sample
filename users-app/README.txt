Simple sample microservices  apps that was writing during research following areas:
  - Spring Cloud
  - Docker\Docker compose

In order to run any full stack just issue: docker-compose up, all services will be launched started
automatically

Consist of couple services:

  - Euereka
  - UserService

Some commands in order to experiment

  mvn clean package dockerfile:build
  Run container on 8090 port
  docker run -t -e DB_PASSWORD=1qaz@WSX -e DB_USERNAME=dockertest -e DB_URL=jdbc:mysql://192.168.1.4:3306/userapp -p 8090:8080 mssamples/userapp



