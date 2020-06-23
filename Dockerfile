FROM openjdk:8-jre-alpine

RUN mkdir little-chat

COPY \
	target/universal/simple-chat-server-0.1.zip little-chat

RUN \
	unzip little-chat/simple-chat-server-0.1.zip -d little-chat && \
	rm -rf little-chat/simple-chat-server-0.1.zip

CMD java -cp "/little-chat/simple-chat-server-0.1/lib/*" com.little.chat.server.MainApplication
