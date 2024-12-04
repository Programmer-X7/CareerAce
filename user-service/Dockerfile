FROM openjdk:17-jdk-slim

COPY target/user-service-1.0.jar /app/user-service.jar

ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip
