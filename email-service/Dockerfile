FROM openjdk:17-jdk-slim

COPY target/email-service-1.0.jar /app/email-service.jar

ENTRYPOINT ["java", "-jar", "/app/email-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip
