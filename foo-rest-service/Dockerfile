# syntax = docker/dockerfile:experimental
# This line above is necessary to activate buildkit on all platforms as of 2021/03.

FROM amazoncorretto:21-alpine

ARG SERVICE
ARG VERSION

ENV SERVICE=$SERVICE
ENV JAVA_OPTS=
ENV JAVA_OPTS_APPEND=

WORKDIR /app/$SERVICE

COPY target/$SERVICE-service-$VERSION.jar /app/$SERVICE/$SERVICE.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS $JAVA_OPTS_APPEND -jar /app/$SERVICE/${SERVICE}.jar" ]
