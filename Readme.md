### This is a sample project that emulates e-commerce  trading system


Demo project where following technologies are used: Spring Cloud, Spring Boot, Docker&Docker-Compose, Java 11,
Maven, Kubernetes.

Databases: Mongo\MySql

Project consist of following micro-services:
* Stub of *Product Catalog* service
* Stub of *Users* service
* Stub of *Stock* service
* Stub of *Payments* service
* Stub of *Stock* service
* Stub of *Order* service

Technologies memo:
* Samples of spring-mvc(rest), jpa, mysql, security, actuator, tests - that's userApp module.
* WebFlux(traditional approach), mongo, test - any, StockApp module
* WebFlux(functional approach), tests - payments module   


TODO:
- [ ] Add cloud gateway sample
- [ ] Add stub of notification service(Kafka\Spring Cloud Bus + dockerization)
- [ ] Implement sample of  API with gRPC
- [ ] Implement sample of API with WebSockets
- [ ] Clearly seperate integration tests from unit tests
- [ ] Swagger Code Gen sample
- [ ] Move to Gradle

In order to run whole stack: *docker-compose -f docker-compose.yml up*
