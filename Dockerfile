FROM openjdk:8-jdk-alpine
MAINTAINER https://github.com/muralidharan-rade
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080	9090