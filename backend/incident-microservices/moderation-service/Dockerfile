# --------- Step 1 - Build the app ---------
FROM maven:3.9.11-eclipse-temurin-21 AS builder
LABEL authors="Ivan"
WORKDIR /app

# Copy parent POM first
COPY ./pom.xml /app/pom.xml
# Copy this service only
COPY ./moderation-service /app/moderation-service

# Install parent POM (no modules)
RUN mvn clean install -N

# Build this service only
WORKDIR /app/moderation-service
RUN mvn clean package -DskipTests


# --------- Step 2 - Run the app ---------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

EXPOSE 8082

COPY --from=builder /app/moderation-service/target/moderation-service-*.jar moderation-service.jar
ENTRYPOINT ["java", "-jar", "moderation-service.jar"]