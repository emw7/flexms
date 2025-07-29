#!/usr/bin/env bash

# Build with ../build.sh.
# Assumes jar to be run is in target/.

# java (javac and mvn for building) must be in the classpath.

# Run with tracing disabled by prefixing the programs with
# JAVA_OPTS='-Dcom.github.emw7.platform.log.trace-enabled=false'
# (example JAVA_OPTS='-Dcom.github.emw7.platform.log.trace-enabled=false' ./run.sh)

# Example
# ./run.sh create 100000 "Sensor A" 1
# ./run.sh delete 100000

APP=ex-platform-service-runtime-0.0.1-SNAPSHOT.jar

java $JAVA_OPTS -jar target/$APP "$@"
