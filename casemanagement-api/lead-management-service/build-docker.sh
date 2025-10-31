#!/bin/bash

echo "Building Lead Management Service Docker Image..."
echo

# Check if Docker is running
if ! docker version >/dev/null 2>&1; then
    echo "ERROR: Docker is not running. Please start Docker and try again."
    exit 1
fi

echo "Docker is running. Building image..."
echo

# Build the Docker image
docker build -t lead-management-service .

if [ $? -eq 0 ]; then
    echo
    echo "SUCCESS: Docker image built successfully!"
    echo "Image name: lead-management-service"
    echo
    echo "To run the complete stack with database:"
    echo "  docker-compose up"
    echo
    echo "To run only the application (requires database):"
    echo "  docker-compose up lead-management-service"
    echo
else
    echo
    echo "ERROR: Docker build failed!"
    echo "Please check the error messages above."
    echo
    exit 1
fi
