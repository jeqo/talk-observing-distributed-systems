FROM azul/zulu-openjdk:8

WORKDIR /usr/share/hello-world-monolith/

ADD target/lib lib
ADD target/hello-world-monolith-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

EXPOSE 8080 8081

CMD ["java", "-jar", "hello-world-monolith-1.0.0-SNAPSHOT.jar", "server", "docker.yml"]