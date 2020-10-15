#
helm uninstall controlcenter
helm uninstall ksql
helm uninstall replicator
helm uninstall connectors
helm uninstall schemaregistry
helm uninstall kafka
helm uninstall zookeeper
helm uninstall operator
#
kubectl delete service --all
kubectl delete pods --all
kubectl delete deployment --all
