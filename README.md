# my-surge-sample

You need to have a running kafka server. Read this [guide](https://kafka.apache.org/quickstart)
to quickly get a kafka server running on your system.

You then need to create two topics, one for events and another for state:

`bin/kafka-topics.sh --create --topic library-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1`

`bin/kafka-topics.sh --create --topic library-state --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1`

Copy commandHandler.js and eventHandler.js from modules/app/src/main/resources to ~/surge/my-js-app on your system

```
curl -X POST -H "content-type: application/json" -i http://localhost:8080/library/books --data '{"action":"CreateBook","data":{"id":"688593a8-8b1f-42a0-a928-68d31cf84209","title":"foo","author":"bar"}}'
```

```
url -X POST -H "content-type: application/json" -i http://localhost:8080/library/books --data '{"action":"UpdateBook","data":{"id":"688593a8-8b1f-42a0-a928-68d31cf84209","title":"fooobar","author":"bar"}}'
```
