FROM openjdk:17-jdk-alpine
LABEL maintainer="dns.aqua.ban@gmail.com"
VOLUME /user-app
ADD build/libs/user-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
