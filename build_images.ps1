param (
    [string]$version
)

if (-not $version) {
    Write-Host "Version parameter is required."
    exit 1
}

docker build -t davidlearner/users:$version -f .\users\Dockerfile .\users\
docker build -t davidlearner/courses:$version -f .\users\Dockerfile .\courses\

docker push davidlearner/users:$version
docker push davidlearner/courses:$version