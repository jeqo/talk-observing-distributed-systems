FROM azul/zulu-openjdk:8

WORKDIR /usr/share/hello-world-monolith/

ADD target/hello-world-monolith-1.0.0-SNAPSHOT.jar .

CMD ["java", "-jar", "hello-world-monolith-1.0.0-SNAPSHOT.jar"]