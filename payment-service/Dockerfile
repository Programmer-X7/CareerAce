FROM openjdk:17-jdk-slim

COPY target/payment-service-1.0.jar /app/payment-service.jar

ENTRYPOINT ["java", "-jar", "/app/payment-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip