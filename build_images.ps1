param (
    [string]$version
)

if (-not $version) {
    Write-Host "Version parameter is required."
    exit 1
}

docker build -t davidlearner/users-service -f .\users\Dockerfile .\users\
docker build -t davidlearner/courses-service -f .\users\Dockerfile .\courses\
docker build -t davidlearner/api-gateway-service -f .\api_gateway\Dockerfile .\api_gateway\
docker build -t davidlearner/auth-service -f .\auth\Dockerfile .\auth\

docker push davidlearner/users-service
docker push davidlearner/courses-service
docker push davidlearner/api-gateway-service
docker push davidlearner/auth-service