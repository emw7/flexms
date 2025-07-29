# Platform :: Examples :: ex-platform-up-down-stream-downstream-api

```shell
$ set -a
$ . configuration.conf
$ set +a
$ ./run.sh

$ curl -X POST -H'Content-type: application/json' -d '{"code":"x","name":"X sensor","type":1}' http://localhost:8701/downstream/v1/sensor

$ curl -X DELETE http://localhost:8701/downstream/v1/sensor/1

$ curl -X GET http://localhost:8701/downstream/v1/sensor/1:read

$ curl -X GET http://localhost:8701/downstream/v1/sensor/unreachable-sensor:read?respondWithError=true

$ curl -X GET http://localhost:8701/downstream/v1/sensor/unreachable-sensor:read?respondWithError=false
```
