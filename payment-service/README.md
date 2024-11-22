# Payment Service

## Structure 

payment-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── paymentservice/
│   │   │               ├── PaymentServiceApplication.java
│   │   │               ├── controller/
│   │   │               │   └── PaymentController.java
│   │   │               ├── model/
│   │   │               │   └── PaymentRequest.java
│   │   │               └── config/
│   │   │                   └── KafkaProducerConfig.java
│   ├── resources/
│       └── application.properties
├── pom.xml


## Testing

7. Testando o Serviço

    Inicie o Kafka: Certifique-se de que o Kafka está rodando (pode usar o docker-compose do projeto).

    Inicie o Payment Service: Compile e rode o Payment Service:

mvn spring-boot:run

Teste a API REST: Use o curl ou uma ferramenta como Postman para testar o endpoint:

curl -X POST http://localhost:8080/api/payments \
-H "Content-Type: application/json" \
-d '{"userId": "user123", "amount": 100.5, "status": "initiated"}'

Verifique as mensagens no Kafka: Leia as mensagens no tópico payment-initiated:

docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic payment-initiated --from-beginning

Você deve ver a mensagem publicada:

    {"userId":"user123","amount":100.5,"status":"initiated"}

Fluxo Completo

Com o Payment Service configurado, ele:

    Recebe pagamentos via API REST.
    Publica mensagens no tópico Kafka (payment-initiated).
    Os outros serviços (como Fraud Detection Service) podem consumir essas mensagens e continuar o processamento.