VERSION=0.11
#
kubectl delete service kafka-pub-sub-stream
kubectl delete deployment kafka-pub-sub-stream
kubectl delete pod `kubectl get pod | grep kafka-pub-sub-stream | awk '{print $1}'`
#
mvn package -DskipTests com.google.cloud.tools:jib-maven-plugin:build -Dimage=registry.hub.docker.com/gregclinker/kafka-pub-sub-stream:$VERSION
kubectl create deployment kafka-pub-sub-stream --image=registry.hub.docker.com/gregclinker/kafka-pub-sub-stream:$VERSION
kubectl create service loadbalancer kafka-pub-sub-stream --tcp=8080:8080
while kubectl get svc kafka-pub-sub-stream | grep pending; do echo "sleeping"; sleep 10; done
kubectl get svc kafka-pub-sub-stream
