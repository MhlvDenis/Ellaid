FROM openjdk:17-jdk-alpine
LABEL maintainer="dns.aqua.ban@gmail.com"
VOLUME /comment-app
ADD build/libs/comment-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
