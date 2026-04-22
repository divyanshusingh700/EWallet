# Dockerfile
FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
RUN apt-get update && apt-get install -y curl
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1