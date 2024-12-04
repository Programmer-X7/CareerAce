FROM openjdk:17-jdk-slim

COPY target/api-gateway-1.0.jar /app/api-gateway.jar

ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]

# Run following commands
# generate .jar file
# ./mvnw clean package -Dmaven.test.skip