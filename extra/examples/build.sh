#!/usr/bin/env bash

EMW7_PLATFORM_DIR=${EMW7_PLATFORM_DIR-../..}

mvn --settings ${EMW7_PLATFORM_DIR}/mvn_settings.xml clean package "$@"
