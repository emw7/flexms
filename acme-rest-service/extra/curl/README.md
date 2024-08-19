# cURL

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