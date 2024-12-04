FROM openjdk:17-jdk-slim

COPY target/roadmap-service-1.0.jar /app/roadmap-service.jar

ENTRYPOINT ["java", "-jar", "/app/roadmap-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip
