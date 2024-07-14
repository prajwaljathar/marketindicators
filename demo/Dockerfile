# Use Eclipse Temurin as the base image
FROM eclipse-temurin:17-jdk-focal AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=build /app/target/Stock-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/src/main/resources/application1.properties application.properties

ENV SPRING_CONFIG_NAME=application1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]