#!/usr/bin/env bash

# Build with mvn package.
# Assumes jar to be run is in target/.

# java (javac and mvn for building) must be in the classpath.


java \
  -Dcom.github.emw7.platform.log.benchmark=false \
  -Dlog4j.configuration=file:../log4j.properties \
  -Dcom.github.emw7.platform.log.log-on-thread=false \
  -classpath ./libs \
  -jar target/reload4j-0.0.1-SNAPSHOT.jar \
    ${1-100}
