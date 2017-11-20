FROM azul/zulu-openjdk:8

WORKDIR /usr/share/hello-world-service/

ADD target/lib lib
ADD target/hello-world-service-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

EXPOSE 8080 8081

CMD ["java", "-jar", "hello-world-service-1.0.0-SNAPSHOT.jar", "server", "docker.yml"]