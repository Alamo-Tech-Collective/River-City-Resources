# Multi-stage build for Grails application
FROM gradle:7.6.4-jdk17-alpine AS builder

# Set working directory
WORKDIR /build

# Copy gradle wrapper and build files first for better caching
COPY gradlew gradlew.bat build.gradle gradle.properties settings.gradle /build/
COPY gradle /build/gradle

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies - this layer will be cached unless dependencies change
RUN ./gradlew dependencies --no-daemon --parallel

# Copy source code
COPY grails-app /build/grails-app
COPY src /build/src

# Build the application
RUN ./gradlew bootJar --no-daemon --parallel

# Runtime stage
FROM openjdk:17-jdk-slim

# Install required packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create app user
RUN useradd -m -u 1000 grails

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder --chown=grails:grails /build/build/libs/*.jar app.jar

# Switch to non-root user
USER grails

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for container environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dgrails.env=production -jar app.jar"]