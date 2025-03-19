#!/bin/bash

# 配置项目 ID 和镜像版本
PROJECT_ID="spheric-engine-448906-m3"
REGION="northamerica-northeast1"
REPOSITORY="frauddetectiondemo"
IMAGE_NAME="frauddetection"
IMAGE_TAG="latest"
#northamerica-northeast1-docker.pkg.dev/spheric-engine-448906-m3/frauddetectiondemo/frauddetection:latest
# 构建完整的镜像路径
IMAGE_PATH="$REGION-docker.pkg.dev/$PROJECT_ID/$REPOSITORY/$IMAGE_NAME:$IMAGE_TAG"

# 错误处理函数
handle_error() {
  echo "[ERROR] $1"
  exit 1
}

# 构建 Docker 镜像
echo "Building Docker image..."
docker build -t $IMAGE_PATH . || handle_error "Failed to build Docker image. Check your Dockerfile and build context."

# 登录 Artifact Registry
echo "Authenticating with Artifact Registry..."
gcloud auth configure-docker $REGION-docker.pkg.dev || handle_error "Failed to authenticate with Artifact Registry. Ensure gcloud is installed and configured."

# 推送镜像到 Artifact Registry
echo "Pushing Docker image to Artifact Registry..."
docker push $IMAGE_PATH || handle_error "Failed to push Docker image. Check your network connection and permissions."

echo "Docker image pushed successfully: $IMAGE_PATH"
