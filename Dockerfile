FROM openjdk:8-jre-alpine

WORKDIR chat-server-app

EXPOSE 8080

COPY target/scala-2.12/simple-chat-server-assembly-0.1.jar .

ENTRYPOINT java -jar simple-chat-server-assembly-0.1.jar
