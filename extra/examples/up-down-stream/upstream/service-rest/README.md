# Platform :: Examples :: ex-platform-up-down-stream-upstream-api

Depends on [downstream](../../downstream/service-rest/README.md) 

```shell
$ set -a
$ . configuration.conf
$ set +a
$ ./run.sh

$ curl -X POST -H'Content-type: application/json' -d '{"code":"x","name":"X sensor","type":1}' http://localhost:8801/upstream/v1/x

$ curl -X GET http://localhost:8801/upstream/v1/x/1:read

$ curl -X GET http://localhost:8801/upstream/v1/x/unreachable-sensor:read?respondWithError=false

$ curl -X GET http://localhost:8801/upstream/v1/x/unreachable-sensor:read?respondWithError=true

$ curl -X DELETE http://localhost:8801/upstream/v1/x/1
```
