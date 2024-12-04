FROM openjdk:17-jdk-slim

COPY target/naming-server-1.0.jar /app/naming-server.jar

ENTRYPOINT ["java", "-jar", "/app/naming-server.jar"]

# Run following commands
# generate .jar file (skip test)
# ./mvnw clean package -Dmaven.test.skip

# to build the image with "latest" tag (for single container)
# docker build -t config-server:latest .

# Run docker container (for single container)
# docker run -p 8888:8888 --name config-server -d config-server:latest 
