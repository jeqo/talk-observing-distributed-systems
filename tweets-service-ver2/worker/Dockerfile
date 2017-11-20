FROM azul/zulu-openjdk:8

WORKDIR /usr/share/tweets-service-ver2/

ADD target/lib lib
ADD target/worker-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

EXPOSE 8080 8081

CMD ["java", "-jar", "worker-1.0.0-SNAPSHOT.jar", "server", "docker.yml"]