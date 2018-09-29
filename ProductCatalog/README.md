Product catalog app

1) Build: mvn clean package
2) Build docker image:  docker build -t product-catalog .  
3) Run docker container:  docker run -t -e PRODUCT-DB-HOST=<hostname>  -e PRODUCT-DB-PORT=27017 -e PRODUCT-DB-NAME=products -e eureka_url=http://172.18.0.3:8761/eureka/ -p 8082:8082 --name product-catalog product-catalog