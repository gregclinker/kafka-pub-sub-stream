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
