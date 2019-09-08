FROM gradle:5.6.2-jdk8 as buildStage
WORKDIR /build

COPY src /build/src
COPY build.gradle /build
COPY settings.gradle /build

RUN gradle clean build

FROM openjdk:8-alpine

WORKDIR /app

COPY --from=buildStage /build/build/libs/scrabble-challenge-1.0-SNAPSHOT-all.jar /app/scrabble-challenge.jar
COPY --from=buildStage /build/build/resources/main /app


ENTRYPOINT ["java", "-jar", "/app/scrabble-challenge.jar", "/app/board.json", "/app/letters.json"]
