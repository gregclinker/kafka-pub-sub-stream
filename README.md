# kafka-pub-sub-stream

Spring Boot/Web Flux/Kafka application that drops a payment on a Kafka event and consumes it in a reactive stream..

**To Build**
```shell script
mvn clean installl
```

**To Run**
```shell script
java -Dspring.profiles.active=dev -jar target/kafka-pub-sub-stream-0.1.jar
```
http://localhost:8080/actuator/health To check the app is up.

Default config for Kafka is set in the Spring Boot application.properties

```
kafka.bootstrapServers=localhost:9092
```
A quick way to get a complete Kafka & Schema Registry up and running is to [run Confluent locally](https://docs.confluent.io/current/quickstart/ce-quickstart.html)  or [run Confluent with Docker](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html). This also gives you a nice front end that you can use to see Kafka messages.

To read a topic from a queue
```
kafka-console-consumer --bootstrap-server localhost:9092 --topic payments --from-beginning
```

**Publisher** http://localhost:8080/testPayment convienience method to create a test payment and write to a topic

**Consumer Stream** http://localhost:8080/ws.html will consume events as a stream

**To Build a Docker Image and Deploy to GCP**
```
mvn package -DskipTests com.google.cloud.tools:jib-maven-plugin:build -Dimage=registry.hub.docker.com/gregclinker/kafka-pub-sub-stream:0.1

kubectl create deployment kafka-pub-sub-stream --image=registry.hub.docker.com/gregclinker/kafka-pub-sub-stream:0.1
kubectl create service loadbalancer kafka-pub-sub-stream --tcp=8080:8080
```

**To Build Full Confluent Kafka Cluster on GCP**
Based on this [Confluent example](https://docs.confluent.io/current/installation/operator/co-deployment.html)
You need to have helm installed and a cluster initialised, the create_kafka.cluster script in the helm directory will build a fill  
```
#!/bin/bash
#
export VALUES_FILE=/home/greg/work/k8s-kafka-example/helm/providers/my-values.yaml
#
helm upgrade --install   operator   ./confluent-operator   --values $VALUES_FILE   --set operator.enabled=true
while ! kubectl get pods | grep operator | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   zookeeper   ./confluent-operator   --values $VALUES_FILE   --set zookeeper.enabled=true
while ! kubectl get pods | grep zookeeper | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   kafka   ./confluent-operator   --values $VALUES_FILE   --set kafka.enabled=true
while ! kubectl get pods | grep kafka | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   schemaregistry   ./confluent-operator   --values $VALUES_FILE   --set schemaregistry.enabled=true
while ! kubectl get pods | grep schemaregistry | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   connectors   ./confluent-operator   --values $VALUES_FILE   --set connect.enabled=true
while ! kubectl get pods | grep connectors | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   replicator   ./confluent-operator   --values $VALUES_FILE   --set replicator.enabled=true
while ! kubectl get pods | grep replicator | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   ksql   ./confluent-operator   --values $VALUES_FILE   --set ksql.enabled=true
while ! kubectl get pods | grep ksql | grep Running; do echo "sleeping"; sleep 5; done
#
helm upgrade --install   controlcenter   ./confluent-operator   --values $VALUES_FILE   --set controlcenter.enabled=true
```

In the example below kafka.bootstrapServers=10.48.2.98:9071 see [confluent endpoints](https://docs.confluent.io/5.4.2/installation/operator/co-endpoints.html):

```
NAME                         TYPE           CLUSTER-IP     EXTERNAL-IP      PORT(S)                                        AGE
connectors                   ClusterIP      None           <none>           8083/TCP,9083/TCP,7203/TCP,7777/TCP            47m
connectors-0-internal        ClusterIP      10.3.245.56    <none>           8083/TCP,9083/TCP,7203/TCP,7777/TCP            47m
controlcenter                ClusterIP      None           <none>           9021/TCP,7203/TCP,7777/TCP                     43m
controlcenter-0-internal     ClusterIP      10.3.254.249   <none>           9021/TCP,7203/TCP,7777/TCP                     43m
controlcenter-bootstrap-lb   LoadBalancer   10.3.240.139   34.89.115.60     80:31401/TCP                                   43m
kafka                        ClusterIP      None           <none>           9071/TCP,9072/TCP,9092/TCP,7203/TCP,7777/TCP   50m
kafka-0-internal             ClusterIP      10.3.241.95    <none>           9071/TCP,9072/TCP,9092/TCP,7203/TCP,7777/TCP   50m
kafka-0-lb                   LoadBalancer   10.3.253.226   34.105.234.232   9092:31163/TCP                                 50m
kafka-bootstrap-lb           LoadBalancer   10.3.245.2     34.89.48.198     9092:30380/TCP                                 50m
kafka-pub-sub-stream         LoadBalancer   10.3.244.155   35.230.146.89    8080:31334/TCP                                 25m
ksql                         ClusterIP      None           <none>           8088/TCP,9088/TCP,9199/TCP,7203/TCP,7777/TCP   44m
ksql-0-internal              ClusterIP      10.3.250.88    <none>           8088/TCP,9088/TCP,9199/TCP,7203/TCP,7777/TCP   44m
kubernetes                   ClusterIP      10.3.240.1     <none>           443/TCP                                        55m
replicator                   ClusterIP      None           <none>           8083/TCP,9083/TCP,7203/TCP,7777/TCP            46m
replicator-0-internal        ClusterIP      10.3.240.6     <none>           8083/TCP,9083/TCP,7203/TCP,7777/TCP            46m
schemaregistry               ClusterIP      None           <none>           8081/TCP,9081/TCP,7203/TCP,7777/TCP            49m
schemaregistry-0-internal    ClusterIP      10.3.252.150   <none>           8081/TCP,9081/TCP,7203/TCP,7777/TCP            49m
zookeeper                    ClusterIP      None           <none>           3888/TCP,2888/TCP,2181/TCP,7203/TCP,7777/TCP   51m
zookeeper-0-internal         ClusterIP      10.3.244.103   <none>           3888/TCP,2888/TCP,2181/TCP,7203/TCP,7777/TCP   51m
```
