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
    mvn dependency:go-offline -pl file-service -am -B -q || true

# Copy source code
COPY common/ common/
COPY file-service/ file-service/

# Build the service
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -pl file-service -am -DskipTests -B -q

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/file-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
