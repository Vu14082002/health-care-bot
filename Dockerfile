FROM openjdk:17

WORKDIR /bot-service

COPY ./target target

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./target/health-care-bot-0.0.1-SNAPSHOT.jar"]
