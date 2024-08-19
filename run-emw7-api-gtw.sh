#!/usr/bin/env bash

docker stop emw7-api-gtw || true

docker run --net emw7-network --rm --name emw7-api-gtw -p 8778:80 --mount type=bind,source=$PWD/default.conf.template,target=/etc/nginx/templates/default.conf.template nginx:1.27

