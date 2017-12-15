FROM jeqo/apache-kafka:0.11.0.0-2.11

ADD kafka-connect-twitter-0.1-jar-with-dependencies.jar libs/
ADD connect-source-standalone.properties .
ADD twitter-source.properties .

#ENV CLASSPATH=kafka-connect-twitter-0.1-jar-with-dependencies.jar

EXPOSE 8083

CMD bin/connect-standalone.sh connect-source-standalone.properties twitter-source.properties
