FROM openjdk:11
MAINTAINER vamsi.emani
COPY build/libs/kubermicronaut-0.1-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "com.nerdysermons.Application"]