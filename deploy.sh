#!/bin/bash

set -e

echo "배포를 시작합니다..."

# 코드 Pull
git pull origin main

# Gradle로 빌드
./gradlew clean build -x test

# Docker 이미지 빌드 및 컨테이너 실행
docker-compose down
docker-compose build
docker-compose up -d

echo "배포가 완료되었습니다." 