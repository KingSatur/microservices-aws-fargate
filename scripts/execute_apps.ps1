function Wait-ForService {
    param (
        [string]$serviceName,
        [string]$namespace = "default"
    )

    Write-Host "Waiting for service $serviceName to be ready..."
    while ($true) {
        $endpoints = kubectl get endpoints $serviceName --namespace $namespace -o json
        $addresses = ($endpoints | ConvertFrom-Json).subsets.addresses
        if ($addresses) {
            Write-Host "Service $serviceName is ready."
            break
        }
        Start-Sleep -Seconds 5
    }
}

# Define the name of the ClusterRoleBinding
$clusterRoleBindingName = "admin"

# Check if the ClusterRoleBinding already exists
$exists = kubectl get clusterrolebinding $clusterRoleBindingName -o json | ConvertFrom-Json

if ($exists) {
    Write-Host "ClusterRoleBinding '$clusterRoleBindingName' already exists."
}
else {
    Write-Host "Creating ClusterRoleBinding '$clusterRoleBindingName'."
    kubectl create clusterrolebinding $clusterRoleBindingName --clusterrole=cluster-admin --serviceaccount=default:default
}

# Create secrets
kubectl apply -f .\secret.yaml

# Create configmaps
kubectl apply -f .\configmap.yaml

# Crear auth pods and expose a service
kubectl apply -f .\deployment-auth.yaml

# Create mysql pod with volumes and expose a service
kubectl apply -f .\mysql-pv.yaml `
    -f .\mysql-pvc.yaml `
    -f .\deployment-mysql.yaml

# Create users pods with volumes and expose a service
kubectl apply -f .\deployment-users.yaml

# Create postgresql pod with volumes and expose a service
kubectl apply -f .\pg-pv.yaml `
    -f .\pg-pvc.yaml `
    -f .\deployment-postgresql.yaml

# Create courses pods with volumes and expose a service
kubectl apply -f .\deployment-courses.yaml

# Create api-gateway pods and expose a service
kubectl apply -f .\deployment-gateway.yaml

# Wait for users-svc to be ready
Wait-ForService -serviceName "courses-svc"

# Wait-ForService -serviceName "users-svc"

# Wait for courses-svc to be ready

# Get URL for users service
# Write-Host "Getting URL for users service and courses service"
# minikube service users-svc courses-svc --url
# minikube service gateway-svc --url

# kubectl delete -f .\deployment-postgresql.yaml `
#                -f .\pg-pvc.yaml `
#                -f .\pg-pv.yaml `
#                -f .\deployment-mysql.yaml `
#                -f .\mysql-pvc.yaml `
#                -f .\mysql-pv.yaml `
#                -f .\deployment-users.yaml `
#                -f .\deployment-courses.yaml `
#                -f .\deployment-gateway.yaml `
#                -f .\deployment-auth.yaml `
#                -f .\configmap.yaml `
#                -f .\secret.yaml