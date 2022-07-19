FROM hseeberger/scala-sbt:graalvm-ce-20.0.0-java8_1.3.12_2.11.12 AS builder

WORKDIR build

COPY . .

RUN sbt clean compile assembly

FROM openjdk:8-jre-alpine

WORKDIR chat-server-app

EXPOSE 8080

COPY --from=builder /root/build/target/scala-2.12/simple-chat-server-assembly-0.1.jar .

ENTRYPOINT java -jar simple-chat-server-assembly-0.1.jar