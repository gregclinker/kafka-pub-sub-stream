#create a cluster
gcloud container clusters create gregs-cluster --num-nodes 2 --enable-autoscaling --min-nodes 1 --max-nodes 16 --machine-type n1-standard-1 --zone europe-west2-a
gcloud container clusters get-credentials gregs-cluster
kubectl create serviceaccount tiller -n kube-system
kubectl create clusterrolebinding tiller --clusterrole=cluster-admin --serviceaccount kube-system:tiller
