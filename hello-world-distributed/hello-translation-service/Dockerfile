FROM azul/zulu-openjdk:8

WORKDIR /usr/share/hello-translation-service/

ADD target/lib lib
ADD target/hello-translation-service-1.0.0-SNAPSHOT.jar .
ADD docker.yml .

EXPOSE 8080 8081

CMD ["java", "-jar", "hello-translation-service-1.0.0-SNAPSHOT.jar", "server", "docker.yml"]