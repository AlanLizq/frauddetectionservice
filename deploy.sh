#!/bin/bash

PROJECT_ID="omega-palace-454013-m1"
REGION="us-central1"
REPOSITORY="frauddetectservice"
IMAGE_NAME="frauddetection"
IMAGE_TAG="latest"
IMAGE_PATH="$REGION-docker.pkg.dev/$PROJECT_ID/$REPOSITORY/$IMAGE_NAME:$IMAGE_TAG"

handle_error() {
  echo "[ERROR] $1"
  exit 1
}

echo "Building Docker image..."
docker build -t $IMAGE_PATH . || handle_error "Failed to build Docker image. Check your Dockerfile and build context."

echo "Authenticating with Artifact Registry..."
gcloud auth configure-docker $REGION-docker.pkg.dev || handle_error "Failed to authenticate with Artifact Registry. Ensure gcloud is installed and configured."

echo "Pushing Docker image to Artifact Registry..."
docker push $IMAGE_PATH || handle_error "Failed to push Docker image. Check your network connection and permissions."

echo "Docker image pushed successfully: $IMAGE_PATH"
