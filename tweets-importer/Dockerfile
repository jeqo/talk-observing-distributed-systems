FROM jeqo/confluent-platform-kafka:3.2.1

ADD kafka-connect-twitter-0.1-jar-with-dependencies.jar .
ADD connect-source-standalone.properties .
ADD twitter-source.properties .

ENV CLASSPATH=kafka-connect-twitter-0.1-jar-with-dependencies.jar

EXPOSE 8083

CMD bin/connect-standalone connect-source-standalone.properties twitter-source.properties
