# --------- Step 1 - Build the app ---------
FROM maven:3.9.11-eclipse-temurin-21 AS builder
LABEL authors="Ivan"
WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests


# --------- Step 2 - Run the app ---------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

EXPOSE 9001

# Kopiranje JAR-a iz builder stage-a
COPY --from=builder /app/target/discovery-*.jar eureka-discovery.jar
ENTRYPOINT ["java", "-jar", "eureka-discovery.jar"]