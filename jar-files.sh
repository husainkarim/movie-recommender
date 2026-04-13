#!/bin/bash
set -e  # Stop script if any command fails

# Array of service directories
services=("backend/user-service" "backend/movie-service" "backend/rating-service" "backend/recommendation-service" "backend/api-gateway")

# Loop through services and build each
for service in "${services[@]}"; do
  echo "========================================"
  echo "Building $service..."
  echo "========================================"
  
  if [ -d "$service" ]; then
    cd "$service"
    mvn clean package -DskipTests
    echo "$service built successfully."
    cd ../..
  else
    echo "Directory $service does not exist, skipping..."
  fi
done

echo "========================================"
echo "All services built successfully!"
echo "========================================"
