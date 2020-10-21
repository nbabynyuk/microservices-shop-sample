### This is a sample project that emulates backend for  e-commerce app

Demo project where following technologies were used: Spring Boot, Docker&Docker-Compose, Java 11,
Maven, Kubernetes.

Databases: Mongo\MySql

Project consist of following micro-services:
* Stub of *Product Catalog* service
* Stub of *Users* service
* Stub of *Stock* service
* Stub of *Payments* service
* Stub of *Stock* service
* Stub of *Order* service
* Stub of *Feedback* service

Technologies memo:
* Samples of spring-mvc(rest), jpa, mysql, security, actuator, tests - that's userApp module.
* WebFlux(traditional approach)& Mongo:  stock module
* WebFlux(functional approach) & Mongo - payments module  
* WebFlux(traditional approach) & Redis - feedbacks module 
* Zuul2 - api-gateway module  

TODO:
- [x] Add cloud gateway sample
- [ ] Add stub of notification service(Kafka\Spring Cloud Bus + dockerization)
- [ ] Implement sample of  API with gRPC
- [ ] Implement sample of API with WebSockets
- [ ] Clearly seperate integration tests from unit tests
- [ ] Swagger Code Gen sample
- [ ] Move to Gradle

In order to run whole stack:
* Kubernetes(minikube) way: all resources are in 'kubernetes-deployment' folder
* Feedbacks module is not containerized

Debug tips: 
In order to install `curl` to the busybox: `kubectl exec -it busybox -- opkg-install curl`
Gratitude to Abdennour TOUMI, https://stackoverflow.com/questions/62847331/is-there-possible-to-install-curl-into-busybox-in-kubernetes-pod

* Docker compose: *docker-compose -f docker-compose.yml up* - outdated, 
not all services might work properly 
