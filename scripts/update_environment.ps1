# Define the path to the JSON file
$jsonFilePath = "microservices-aws-fargate-local.postman_environment.json"

# Read the JSON file
$jsonContent = Get-Content -Path $jsonFilePath -Raw | ConvertFrom-Json

# Function to update the port for a given service
function Update-ServicePort {
    param (
        [string]$serviceName
    )

    # Find the service in the JSON object
    $service = $jsonContent.values | Where-Object { $_.key -eq $serviceName }

    if ($service) {
        # Extract the current port
        $currentPort = $service.value -replace 'http://host.docker.internal:', ''

        # Prompt the user for the new port
        $newPort = Read-Host "Enter the new port for $serviceName (current port: $currentPort)"

        # Update the service URL with the new port
        $service.value = "http://host.docker.internal:$newPort"
    }
    else {
        Write-Host "Service $serviceName not found in the JSON file."
    }
}

# List of services to update
$services = @("user_service", "api_gateway_service", "course_service", "auth_service")

# Update the port for each service
foreach ($service in $services) {
    Update-ServicePort -serviceName $service
}

# Save the updated JSON content back to the file
$jsonContent | ConvertTo-Json -Depth 32 | Set-Content -Path $jsonFilePath

Write-Host "The services have been updated successfully."