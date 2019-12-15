FROM openjdk:8u232

RUN apt-get update && apt-get upgrade -y

ARG SCALA_VERSION=2.13.1
ARG SBT_VERSION=1.3.4

RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb

RUN \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

WORKDIR /usr/local/

RUN \
  curl -L -o scala-$SCALA_VERSION.tgz https://downloads.lightbend.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz && \
  tar zxvf scala-$SCALA_VERSION.tgz

ENV PATH="/usr/local/scala-$SCALA_VERSION/bin:${PATH}"

WORKDIR /code
