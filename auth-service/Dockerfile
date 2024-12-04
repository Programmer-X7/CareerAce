FROM openjdk:17-jdk-slim

COPY target/auth-service-1.0.jar /app/auth-service.jar

ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip