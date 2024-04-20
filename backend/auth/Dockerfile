FROM openjdk:17-jdk-alpine
LABEL maintainer="dns.aqua.ban@gmail.com"
ADD build/libs/auth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
