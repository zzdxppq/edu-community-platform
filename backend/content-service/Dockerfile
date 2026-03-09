# ---- Build Stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy parent POM and all module POMs for dependency resolution
COPY pom.xml .
COPY common/pom.xml common/
COPY gateway/pom.xml gateway/
COPY core-service/pom.xml core-service/
COPY content-service/pom.xml content-service/
COPY file-service/pom.xml file-service/

# Download dependencies (cached layer)
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -pl content-service -am -B -q || true

# Copy source code
COPY common/ common/
COPY content-service/ content-service/

# Build the service
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -pl content-service -am -DskipTests -B -q

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/content-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
