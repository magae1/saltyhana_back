FROM openjdk:17

WORKDIR spring

COPY build/libs .

ENTRYPOINT ["java", "-jar", "./saltyhanaserver-0.0.1-SNAPSHOT.jar"]