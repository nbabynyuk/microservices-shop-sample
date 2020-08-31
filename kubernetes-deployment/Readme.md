In order to run stack on minikube env 
0) Assuming you are in the folder <kubernetes-deployment> 
1) Start minikube ```minikube start --vm-driver=virtualbox```
2) Build all images in minikube env : ```eval $(minikube docker-env)``` 
Than build images that will be used in sample```docker build -t <you_tags> -f <your_docker files>  ``
3) Define all environment variables. Set  db password variable:
 run following command: ```export USER_APP_DB_PWD=<you_value_here>```
4) Run stack: ```kubectl apply -k .```
5) Delete stack once you finished ```kubectl delete -k .```  

  