## ===== Builder stage =====
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy only pom and resolve dependencies (better caching)
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy source and build
COPY src src
RUN mvn -B -DskipTests -Dmaven.test.skip=true clean package

## ===== Runtime stage =====
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Install curl for healthcheck
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

# Copy built jar from builder
COPY --from=build /app/target/*.jar app.jar

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]
