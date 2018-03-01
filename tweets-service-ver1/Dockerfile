FROM azul/zulu-openjdk:8

ENV DB_URL=jdbc:tracing:postgresql://tweets-db/postgres \
    TRACING_PROVIDER=JAEGER

WORKDIR /usr/share/tweets-service-ver1/

ADD target/lib lib
ADD target/tweets-service-ver1-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

EXPOSE 8080 8081

CMD ["java", "-jar", "tweets-service-ver1-1.0.0-SNAPSHOT.jar", "server", "docker.yml"]