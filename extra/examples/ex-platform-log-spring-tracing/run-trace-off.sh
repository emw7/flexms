#!/usr/bin/env bash

# Run example with tracing on.
# Refer to run.sh for more details.

JAVA_OPTS='-Dcom.github.emw7.platform.log.trace-enabled=false' ./run.sh "$@"
