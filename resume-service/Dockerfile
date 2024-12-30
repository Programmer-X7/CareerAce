FROM openjdk:17-jdk-slim

COPY target/resume-service-1.0.jar /app/resume-service.jar

ENTRYPOINT ["java", "-jar", "/app/resume-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip