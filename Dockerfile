FROM gradle:6.1.1-jdk11 as builder
COPY --chown=gradle:gradle . /home/application
WORKDIR /home/application
RUN gradle shadowJar --no-daemon -x test

FROM amazonlinux:2018.03.0.20200318.1 as graalvm

ENV LANG=en_US.UTF-8

RUN yum install -y gcc gcc-c++ libc6-dev  zlib1g-dev \
    curl bash zlib zlib-devel zip

ENV GRAAL_VERSION 20.2.0-dev
ENV GRAAL_FILENAME graalvm-ce-java11-linux-amd64-${GRAAL_VERSION}.tar.gz

RUN curl -4 -L https://github.com/graalvm/graalvm-ce-dev-builds/releases/download/${GRAAL_VERSION}-20200429_0231/${GRAAL_FILENAME} -o /tmp/${GRAAL_FILENAME}

RUN tar -zxvf /tmp/${GRAAL_FILENAME} -C /tmp \
    && mv /tmp/graalvm-ce-java11-${GRAAL_VERSION} /usr/lib/graalvm

RUN rm -rf /tmp/*
CMD ["/usr/lib/graalvm/bin/native-image"]

FROM graalvm
COPY --from=builder /home/application/ /home/application/
WORKDIR /home/application
RUN /usr/lib/graalvm/bin/gu install native-image
RUN /usr/lib/graalvm/bin/native-image \
    --no-server \
    -cp build/libs/quarantinebot.jar
RUN chmod 755 quarantinebot
RUN chmod 755 bootstrap
RUN zip -j function.zip bootstrap quarantinebot