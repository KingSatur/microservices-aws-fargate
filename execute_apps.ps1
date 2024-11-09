# Create secrets
kubectl apply -f .\secret.yaml

# Create configmaps
kubectl apply -f .\configmap.yaml

# Create mysql pods with volumes and expose a service
kubectl apply -f .\mysql-pv.yaml `
              -f .\mysql-pvc.yaml `
              -f .\deployment-mysql.yaml

# Create users pods with volumes and expose a service
kubectl apply -f .\deployment-users.yaml

# Create postgresql pods with volumes and expose a service
kubectl apply -f .\pg-pv.yaml `
              -f .\pg-pvc.yaml `
              -f .\deployment-postgresql.yaml

# Create courses pods with volumes and expose a service
kubectl apply -f .\deployment-courses.yaml

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

# Wait for users-svc to be ready
Wait-ForService -serviceName "users-svc"

# Wait for courses-svc to be ready
Wait-ForService -serviceName "courses-svc"

# Get URL for users service
Write-Host "Getting URL for users service and courses service"
minikube service users-svc courses-svc --url

# kubectl delete -f .\deployment-postgresql.yaml `
#                -f .\pg-pvc.yaml `
#                -f .\pg-pv.yaml `
#                -f .\deployment-mysql.yaml `
#                -f .\mysql-pvc.yaml `
#                -f .\mysql-pv.yaml `
#                -f .\deployment-users.yaml `
#                -f .\deployment-courses.yaml `
#                -f .\configmap.yaml `
#                -f .\secret.yaml