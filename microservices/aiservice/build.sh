#!/bin/bash

# Build AI Service Docker Image
echo "Building AI Service Docker Image..."

cd "$(dirname "$0")"

docker build -t aiservice:latest .

if [ $? -eq 0 ]; then
    echo "✅ AI Service image built successfully!"
    echo "Image: aiservice:latest"
    echo ""
    echo "To run the service:"
    echo "  docker-compose up -d"
    echo ""
    echo "To test the service:"
    echo "  curl http://localhost:8086/health"
else
    echo "❌ Build failed!"
    exit 1
fi
