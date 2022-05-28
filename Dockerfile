FROM openjdk:17
VOLUME /tmp
ADD target/ShortenUrlRestful-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
