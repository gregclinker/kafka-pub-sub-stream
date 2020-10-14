# kafka-pub-sub-stream

Spring Boot/Web Flux/Kafka application that drops a payment on a Kafka event and consumes it in a reactive stream..

**To Build**
```shell script
mvn clean installl
```

**To Run**
```shell script
java -jar target/kafka-pub-sub-stream-0.1.jar
```
http://localhost:8080/actuator/health To check the app is up.

Default config for Kafka is set in the Spring Boot application.properties

```
kafka.bootstrapServers=localhost:9092
```
A quick way to get a complete Kafka & Schema Registry up and running is to [run Confluent locally](https://docs.confluent.io/current/quickstart/ce-quickstart.html)  or [run Confluent with Docker](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html). This also gives you a nice front end that you can use to see Kafka messages.

**Publisher** http://localhost:8080/testPayment convienience method to create a test payment and write to a topic

**Consumer Stream** http://localhost:8080/ws.html will consume events as a stream

**To Build a Docker Image and Deploy to GCP**
```
mvn package -DskipTests com.google.cloud.tools:jib-maven-plugin:build -Dimage=registry.hub.docker.com/gregclinker/kafka-pub-sub-stream:0.1

kubectl create deployment api-simulator --image=registry.hub.docker.com/gregclinker/kafka-pub-sub-stream:0.1
kubectl create service loadbalancer kafka-pub-sub-stream --tcp=8080:8080
```
