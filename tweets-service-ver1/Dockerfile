FROM azul/zulu-openjdk:8

ENV TWEETS_DB_URL="jdbc:tracing:postgresql://tweets-db/postgres"
ENV TWEETS_TRACING_PROVIDER="JAEGER"

WORKDIR /usr/share/tweets-service-ver1/

ADD target/lib lib
ADD target/tweets-service-ver1-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

#RUN mkdir config
#ADD skywalking-agent/ lib/

EXPOSE 8080 8081

CMD ["java", \
        #"-Dskywalking.agent.application_code=tweets-service-v1", \
        #"-Dskywalking.collector.servers=skywalking-collector:10800", \
        #"-javaagent:tweets-service-ver1-1.0.0-SNAPSHOT.jar", \
        "-jar", \
        "tweets-service-ver1-1.0.0-SNAPSHOT.jar", \
        "server", \
        "docker.yml"]