# Payment Processing Platform

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