# 빌드
FROM node:18-alpine AS build

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 설치
COPY package*.json ./
RUN npm install

# 애플리케이션 파일 추가
COPY . .

# 애플리케이션 빌드
RUN npm run build-only

# 최종 이미지 설정
FROM nginx:alpine

# 애플리케이션 파일 복사
COPY --from=build /app/dist /usr/share/nginx/html

# 컨테이너 실행 시 실행할 명령 정의
CMD ["nginx", "-g", "daemon off;"]
