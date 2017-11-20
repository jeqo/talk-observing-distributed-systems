FROM azul/zulu-openjdk:8

WORKDIR /usr/share/tweets-producer/

ADD target/lib lib
ADD target/tweets-producer-1.0.0-SNAPSHOT.jar .

CMD ["java", "-jar", "tweets-producer-1.0.0-SNAPSHOT.jar"]