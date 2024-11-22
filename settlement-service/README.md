# Sttlement Service

## Structure

settlement-service/
├── Cargo.toml
├── src/
│   ├── main.rs
│   ├── kafka.rs
│   ├── settlement.rs



## Test

Teste do Serviço

    Produza mensagens no tópico payment-fraud-checked:

docker exec -it kafka kafka-console-producer --bootstrap-server localhost:9092 --topic payment-fraud-checked

Exemplo de mensagem:

{"user_id": "user123", "amount": 1200.0, "status": "valid"}

Consuma mensagens do tópico payment-settled:

    docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic payment-settled --from-beginning

Resultado

    Mensagens válidas (status: "valid") serão processadas e publicadas no tópico payment-settled com o timestamp de liquidação.
    Mensagens inválidas serão ignoradas e logadas como "não liquidadas".