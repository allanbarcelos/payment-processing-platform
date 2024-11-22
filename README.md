# Payment Processing Platform

- **Payment Service:** Manages payments.
- **Notification Service:** Sends notifications to users.
- **Fraud Detection Service:** Verifies the authenticity of transactions.
- **Settlement Service:** Processes payment settlement.

## Structure

```
payment-platform/
├── payment-service/
│   ├── src/
│   ├── Dockerfile
├── kafka/
│   ├── docker-compose.yml
│   ├── zookeeper/ # Configuração do Zookeeper
│   ├── kafka/     # Configuração do Kafka
├── database/
│   ├── docker-compose.yml
│   ├── postgres/  # Configuração do PostgreSQL
├── docker-compose.yml
```