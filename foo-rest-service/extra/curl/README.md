# cURL

UPDATE WITH THE FOLLOWING COPY AND PASTE FROM CONSOLE

em7@em7:~/enrico/git/flexms$ curl -XPOST -H "Content-Type: application/json" --data '{"name":"foo","i":1}' 'http://localhost:8778/foo/v1/foo'
{"id":"3c28941c-a078-4bbe-b2c6-a7c0e0ba6808","name":"foo","i":1,"creationTime":"2024-07-23T13:52:52.346860628+02:00"}em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$ curl -XGET -H "Content-Type: application/json" 'http://localhost:8778/foo/v1/foo/3c28941c-a078-4bbe-b2c6-a7c0e0ba6808'
{"id":"3c28941c-a078-4bbe-b2c6-a7c0e0ba6808","name":"foo","i":835973014,"creationTime":"2024-07-23T13:53:26.869807232+02:00"}em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$ curl -XPUT -H "Content-Type: application/json" --data '{"name":"Leon","i":2}' 'http://localhost:8778/foo/v1/foo/3c28941c-a078-4bbe-b2c6-a7c0e0ba6808'
{"id":"3c28941c-a078-4bbe-b2c6-a7c0e0ba6808","name":"Leon","i":2,"creationTime":"2024-07-23T13:54:03.628692769+02:00"}em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$ curl -XPATCH -H "Content-Type: application/json" --data '{"resetName":true}' 'http://localhost:8778/foo/v1/foo/3c28941c-a078-4bbe-b2c6-a7c0e0ba6808'
{"id":"3c28941c-a078-4bbe-b2c6-a7c0e0ba6808","name":null,"i":835973014,"creationTime":"2024-07-23T13:54:26.741187592+02:00"}em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$ curl -XDELETE -H "Content-Type: application/json" 'http://localhost:8778/foo/v1/foo/3c28941c-a078-4bbe-b2c6-a7c0e0ba6808'
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$ curl -XPOST -H "Content-Type: application/json" --data '{"ids":["3c28941c-a078-4bbe-b2c6-a7c0e0ba6808"]}' 'http://localhost:8778/foo/v1/foo/op:delete-all'
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$
em7@em7:~/enrico/git/flexms$

```shell
# POST / create
curl -XPOST -H "Content-Type: application/json" --data '{"name":"resource-1","i":1}' 'http://localhost:8778/server/vx/acme/foo'

# output:
# {"id":"ecefe5ce-a2e4-452a-a70f-f59677d681ce","name":"resource-1","i":1}
```

```shell
# GET / retrieve
curl -XGET 'http://localhost:8778/server/vx/acme/foo/1ea07d00-e077-4d7a-9b0f-dc3d0ae198bf'

# output:
# {"id":"1ea07d00-e077-4d7a-9b0f-dc3d0ae198bf","name":"","i":0}
```

```shell
# GET / list
curl -XGET 'http://localhost:8778/server/vx/acme/foo?imin=5&imax=10'

# output: 
# {"content":[{"id":"0ce816ad-d984-43a7-9671-c114135695ad","name":"foo-8","i":8}],"page":{"size":20,"number":0,"totalElements":1,"totalPages":1}}
```

```shell
# PUT / update
curl -XPUT -H "Content-Type: application/json" --data '{"name":"resource-1","i":1}' 'http://localhost:8778/server/vx/acme/foo/bea45f99-07e9-40bf-b5ac-793475e5afb2'

# output:
# {"id":"bea45f99-07e9-40bf-b5ac-793475e5afb2","name":"resource-1","i":1}
```

```shell
# DELETE / delete
curl -v -XDELETE 'http://localhost:8778/server/vx/acme/foo/37c704c2-0d2f-485b-950b-bdf9e9728ead'

# output:
```