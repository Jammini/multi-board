# 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 전체 프로젝트 복사 및 빌드
COPY . /app
RUN gradle clean build -x test --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/admin_board.jar

# 애플리케이션 실행
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "-Dspring.profiles.active=dev", "/app/admin_board.jar"]
