FROM openjdk:17-jdk-alpine
COPY target/bankservice-0.0.1.jar bankservice-0.0.1.jar
ENTRYPOINT ["java","-jar","bankservice-0.0.1.jar"]