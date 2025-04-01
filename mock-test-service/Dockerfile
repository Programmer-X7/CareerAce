FROM openjdk:17-jdk-slim

COPY target/mock-test-service-1.0.jar /app/mock-test-service.jar

ENTRYPOINT ["java", "-jar", "/app/mock-test-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip