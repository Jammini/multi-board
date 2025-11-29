# 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 전체 프로젝트 복사 및 빌드
COPY . /app
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/board-server/build/libs/*.jar /app/board.jar

# 애플리케이션 실행
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "-Dspring.profiles.active=local", "/app/board.jar"]
