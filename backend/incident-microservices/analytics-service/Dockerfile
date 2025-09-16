# --------- Step 1 - Build the app ---------
FROM maven:3.9.11-eclipse-temurin-21 AS builder
LABEL authors="Ivan"
WORKDIR /app

# Copy parent POM first
COPY ./pom.xml /app/pom.xml
# Copy this service only
COPY ./analytics-service /app/analytics-service

# Install parent POM (no modules)
RUN mvn clean install -N

# Build this service only
WORKDIR /app/analytics-service
RUN mvn clean package -DskipTests


# --------- Step 2 - Run the app ---------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

EXPOSE 8083

COPY --from=builder /app/analytics-service/target/analytics-service-*.jar analytics-service.jar
ENTRYPOINT ["java", "-jar", "analytics-service.jar"]