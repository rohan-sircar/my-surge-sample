# my-surge-sample

You need to have a running kafka server. Read this [guide](https://kafka.apache.org/quickstart)
to quickly get a kafka server running on your system.

You then need to create two topics, one for events and another for state:

`bin/kafka-topics.sh --create --topic library-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1`

`bin/kafka-topics.sh --create --topic library-store --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1`

You can create a book account this way:

```
curl -X POST -H "content-type: application/json" \
-i http://localhost:8080/library/books \
--data'{"authorName":"foobar","title":"verysecret"}'
```

And retrieve a book this way:
`curl -X GET http://localhost:8080/library/books/4317c6cf-438c-4f36-8391-7b6b36a0e2d9`

And delete:
`curl -X DELETE http://localhost:8080/library/books/4317c6cf-438c-4f36-8391-7b6b36a0e2d9`
