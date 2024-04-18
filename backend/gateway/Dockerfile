FROM openjdk:17-jdk-alpine
LABEL maintainer="dns.aqua.ban@gmail.com"
VOLUME /gateway-app
ADD build/libs/gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "-Xdebug"]
