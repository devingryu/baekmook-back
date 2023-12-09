# Stage 1: Build the application
FROM azul/zulu-openjdk-alpine:17 AS build
WORKDIR /app

# Copy project files
COPY gradle gradle/
COPY *.gradle.kts gradlew ./

RUN ./gradlew --help > /dev/null

COPY src src

# Build the application using the bootJar task
RUN ./gradlew bootJar --no-daemon

# Stage 2: Create the runtime container
FROM gcr.io/distroless/java17-debian11
WORKDIR /app

ENV MYSQL_URL=mysql:3306 \
    MYSQL_DATABASE=SPRING \
    MYSQL_USER= \
    MYSQL_PASSWORD= \
    JWT_SECRET= \
    ACCESS_TOKEN_EXPIRY_MS=1800000 \
    MASTER_USERNAME= \
    MASTER_PASSWORD= 

# Copy the built application from the build stage
COPY --from=build /app/build/libs/*.jar ./application.jar

# Set the command to run the application
CMD ["application.jar"]