FROM openjdk:17-jdk-slim

COPY target/course-service-1.0.jar /app/course-service.jar

ENTRYPOINT ["java", "-jar", "/app/course-service.jar"]

# generate .jar file
# ./mvnw clean package -Dmaven.test.skip