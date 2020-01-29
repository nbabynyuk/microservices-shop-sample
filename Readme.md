### This is a sample project that emulates e-commerce  trading system
Here I improve my practical skills while investigating technologies 

that are new to me; it serves as a:
* test bed for learning new things
* source of ready-for-usage snippets that I may apply in my day-to-day
work.

Technologies: Spring Cloud, Spring Boot, Docker&Docker Compose, Java 11,
Maven, Kubernetes(a little).

Databases: Mongo\MySql

Project consist of following micro-services:
* Stub of *Product Catalog* service
* Stub of *Users* service
* Stub of *Stock* service
* Stub of *Payments* service
* Stub of *Stock* service
* Stub of *Order* service
* Eureka as service discovery component

Technologies memo:
* Samples of spring-mvc(rest), jpa, mysql, security, actuator, tests - that's userApp module.
* WebFlux(traditional approach), mongo, test - any, StockApp module
* WebFlux(functional approach), tests - payments module   

Userapp contains own docker-compose file that brings mysql or mongo db 
and Eureka server

TODO:
- [ ] Add sample of config storage component
- [ ] Add cloud gateway sample
- [ ] Add stub of notification service(Kafka\Spring Cloud Bus + dockerization)
- [ ] Implement sample of  API with gRPC
- [ ] Implement sample of API with WebSockets
- [ ] Clearly seperate integration tests from unit tests
- [ ] Swagger Code Gen sample
- [ ] Move to Gradle

In order to run whole stack: *docker-compose -f docker-compose.yml up*
