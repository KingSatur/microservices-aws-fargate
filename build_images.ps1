param (
    [string]$version = "latest"
)

# Define the services and their Dockerfile paths
$services = @(
    [PSCustomObject]@{ Name = "users-service"; Path = ".\users" }
    [PSCustomObject]@{ Name = "courses-service"; Path = ".\courses" }
    [PSCustomObject]@{ Name = "gateway-service"; Path = ".\gateway" }
    [PSCustomObject]@{ Name = "auth-service"; Path = ".\auth" }
)
# Display the selection menu
Write-Host "Select the service to build:"
$index = 1
foreach ($service in $services) {
    Write-Host "$index. $($service.Name)"
    $index++
}

# Get the user's selection
$selection = Read-Host "Enter the number of the service to build"

if ($selection -lt 1 -or $selection -gt $services.Count) {
    Write-Host "Invalid selection."
    exit 1
}

# Get the selected service and Dockerfile path
$selectedService = $services[$selection - 1]
$selectedServiceName = $selectedService.Name
$dockerfilePath = "$($selectedService.Path)\Dockerfile"

# Ensure the Docker image name and tag are correctly formatted
$imageName = "davidlearner/${selectedServiceName}:$version"

$ErrorActionPreference = "Stop"

try {
    # Build the Docker image
    $buildResult = docker build -t $imageName -f $dockerfilePath $selectedService.Path
    if ($LASTEXITCODE -ne 0) {
        throw "Docker build failed for $selectedServiceName"
    }
    Write-Host "Docker image for $selectedServiceName built successfully with tag $version."

    # Push the Docker image
    $pushResult = docker push $imageName
    if ($LASTEXITCODE -ne 0) {
        throw "Docker push failed for $selectedServiceName"
    }
    Write-Host "Docker image for $selectedServiceName pushed successfully."
}
catch {
    Write-Host "An error occurred: $_"
    exit 1
}