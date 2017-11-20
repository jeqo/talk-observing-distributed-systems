FROM azul/zulu-openjdk:8

WORKDIR /usr/share/hello-world-client/

ADD target/lib lib
ADD target/hello-world-client-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

EXPOSE 8080 8081

CMD ["java", "-jar", "hello-world-client-1.0.0-SNAPSHOT.jar", "server", "docker.yml"]