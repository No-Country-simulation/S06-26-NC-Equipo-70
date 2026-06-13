FROM eclipse-temurin:25-jdk-noble AS deps

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw \
    && ./mvnw -q -DskipTests dependency:go-offline

FROM eclipse-temurin:25-jdk-noble AS build

WORKDIR /workspace

COPY --from=deps /workspace /workspace
COPY src/ src/

RUN chmod +x mvnw \
    && ./mvnw -q -DskipTests package \
    && find target -maxdepth 1 -type f \
        -name "*.jar" \
        ! -name "*.jar.original" \
        -exec cp {} /workspace/app.jar \; \
    && test -f /workspace/app.jar

FROM gcr.io/distroless/java25-debian13:nonroot AS runtime

ENV APP_HOME=/app
ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR ${APP_HOME}

COPY --from=build --chown=65532:65532 \
    /workspace/app.jar \
    ${APP_HOME}/app.jar

USER 65532:65532

EXPOSE 8080

CMD ["/app/app.jar"]