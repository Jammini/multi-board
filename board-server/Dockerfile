FROM openjdk:17
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=dev
ENTRYPOINT ["java", "-jar", "/app.jar"]
