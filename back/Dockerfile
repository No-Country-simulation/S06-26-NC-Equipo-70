FROM eclipse-temurin:25-jdk-alpine AS deps

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw \
    && ./mvnw \
        --batch-mode \
        --quiet \
        -DskipTests \
        dependency:go-offline

FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /workspace

COPY --from=deps /workspace /workspace
COPY src/ src/

RUN ./mvnw \
    --batch-mode \
    --quiet \
    -DskipTests \
    package \
    && find target \
        -maxdepth 1 \
        -type f \
        -name "*.jar" \
        ! -name "*.jar.original" \
        -exec mv {} /workspace/app.jar \; \
    && test -f /workspace/app.jar

FROM eclipse-temurin:25-jdk-alpine AS jre-builder

RUN jlink \
    --add-modules \
        java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,java.xml,java.compiler,jdk.unsupported,jdk.crypto.ec \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=zip-9 \
    --output /custom-jre

FROM alpine:3.24 AS runtime

ENV APP_HOME=/app \
    SERVER_PORT=8080 \
    MANAGEMENT_SERVER_PORT=9090 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_HOME=/opt/java/openjdk \
    PATH="/opt/java/openjdk/bin:${PATH}" \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=65.0 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

WORKDIR /app

RUN addgroup \
    --system \
    --gid 65532 \
    appgroup \
    && adduser \
        --system \
        --uid 65532 \
        --ingroup appgroup \
        --home /app \
        --shell /sbin/nologin \
        appuser

COPY --from=jre-builder \
    /custom-jre \
    /opt/java/openjdk

COPY --from=build \
    --chown=appuser:appgroup \
    /workspace/app.jar \
    /app/app.jar

USER appuser

EXPOSE 8080 9090

HEALTHCHECK \
    --interval=30s \
    --timeout=5s \
    --start-period=60s \
    --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:9090/actuator/health/liveness || exit 1

ENTRYPOINT \
    [ \
        "java", \
        "-jar", \
        "/app/app.jar" \
    ]