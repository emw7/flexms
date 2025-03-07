#!/usr/bin/env bash

# Assumes to be run from project root.
# Output jar will be in target/

# java (javac and mvn for building) must be in the classpath.

EMW7_PLATFORM_DIR="${EMW7_PLATFORM_DIR-../../../../..}"

mvn --settings "${EMW7_PLATFORM_DIR}"/mvn_settings.xml clean package
