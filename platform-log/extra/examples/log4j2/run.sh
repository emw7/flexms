#!/usr/bin/env bash

# Build with mvn package.
# Assumes to be run from target/.

# java (javac and mvn for building) must be in the classpath.


java \
  -Dcom.github.emw7.platform.log.benchmark=false \
  -Dlog4j2.configurationFile=../log4j2.xml \
  -Dcom.github.emw7.platform.log.log-on-thread=false \
  -classpath ./libs \
  -jar platform-log-examples-log4j2-0.0.1-SNAPSHOT.jar \
  2000000
