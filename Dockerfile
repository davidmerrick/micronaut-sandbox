FROM gradle:6.1.1-jdk11 AS builder
COPY --chown=gradle:gradle . /home/application
WORKDIR /home/application

RUN gradle shadowJar --no-daemon -x test

FROM openjdk:11.0.7-slim
COPY --from=builder /home/application/build/libs/quarantinebot.jar \
    /home/application/

WORKDIR /home/application

CMD ["java", "-jar", "quarantinebot.jar"]