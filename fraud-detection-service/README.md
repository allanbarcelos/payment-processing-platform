# Fraud detection Service

## Structure

fraud-detection-service/
├── build.sbt
├── src/
│   ├── main/
│   │   ├── scala/
│   │   │   ├── Main.scala
│   │   │   ├── KafkaConfig.scala
│   │   │   ├── FraudDetectionService.scala
│   │   │   └── models/
│   │   │       └── Payment.scala


## Docker

```shell
docker exec -it kafka kafka-topics --create --bootstrap-server localhost:9092 --topic payment-initiated --partitions 3 --replication-factor 1

docker exec -it kafka kafka-topics --create --bootstrap-server localhost:9092 --topic payment-fraud-checked --partitions 3 --replication-factor 1
```

## Test



    Produza mensagens no tópico payment-initiated:

docker exec -it kafka kafka-console-producer --bootstrap-server localhost:9092 --topic payment-initiated

Exemplo de mensagem:

{"userId": "user123", "amount": 1200, "status": "initiated"}

Consuma mensagens do tópico payment-fraud-checked:

    docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic payment-fraud-checked --from-beginning

