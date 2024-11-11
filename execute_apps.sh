#!/bin/bash

# Function to wait for a service to be ready
wait_for_service() {
    local serviceName=$1
    local namespace=${2:-default}

    echo "Waiting for service $serviceName to be ready..."
    while true; do
        endpoints=$(kubectl get endpoints $serviceName --namespace $namespace -o json)
        addresses=$(echo $endpoints | jq -r '.subsets[].addresses')
        if [ -n "$addresses" ]; then
            echo "Service $serviceName is ready."
            break
        fi
        sleep 5
    done
}

# Define the name of the ClusterRoleBinding
clusterRoleBindingName="admin"

# Check if the ClusterRoleBinding already exists
exists=$(kubectl get clusterrolebinding $clusterRoleBindingName -o json)

if [ -n "$exists" ]; then
    echo "ClusterRoleBinding '$clusterRoleBindingName' already exists."
else
    echo "Creating ClusterRoleBinding '$clusterRoleBindingName'."
    kubectl create clusterrolebinding $clusterRoleBindingName --clusterrole=cluster-admin --serviceaccount=default:default
fi

# Create secrets
kubectl apply -f ./secret.yaml

# Create configmaps
kubectl apply -f ./configmap.yaml

# Create mysql pods with volumes and expose a service
kubectl apply -f ./mysql-pv.yaml \
              -f ./mysql-pvc.yaml \
              -f ./deployment-mysql.yaml

# Create users pods with volumes and expose a service
kubectl apply -f ./deployment-users.yaml

# Create postgresql pods with volumes and expose a service
kubectl apply -f ./pg-pv.yaml \
              -f ./pg-pvc.yaml \
              -f ./deployment-postgresql.yaml

# Create courses pods with volumes and expose a service
kubectl apply -f ./deployment-courses.yaml

# Create api-gateway pods and expose a service
kubectl apply -f .\deployment-gateway.yaml

# Wait for users-svc to be ready
wait_for_service "users-svc"

# Wait for courses-svc to be ready
wait_for_service "courses-svc"

# Get URL for users service and courses service
echo "Getting URL for users service and courses service"

minikube service users-svc courses-svc --url

# kubectl delete -f .\deployment-postgresql.yaml \
#                -f .\pg-pvc.yaml \
#                -f .\pg-pv.yaml \
#                -f .\deployment-mysql.yaml \
#                -f .\mysql-pvc.yaml \
#                -f .\mysql-pv.yaml \
#                -f .\deployment-users.yaml \
#                -f .\deployment-courses.yaml \
#                -f .\configmap.yaml \
#                -f .\secret.yaml