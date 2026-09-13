# Multi-stage Dockerfile for CorporateTravel360 Enterprise Platform
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x ./gradlew && ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

LABEL maintainer="AeroCorp Technologies <ops@corporatetravel360.com>"
LABEL version="1.0.0"
LABEL description="CorporateTravel360 - Enterprise Corporate Business Travel & Expense Platform"

COPY --from=build /workspace/app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
