# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y maven && apt-get clean

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build artifact into the container at /app
COPY target/*.jar /app/app.jar
RUN mvn clean install -DskipTests
# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
