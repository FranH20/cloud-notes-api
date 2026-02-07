# Optional Dockerfile (multi-stage) for consistent Java 21 runtime in Azure
# Why: App Service runtime availability can vary by region; Docker guarantees Java version and startup command.

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests=false clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/cloud-notes-api-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar /app/app.jar"]

