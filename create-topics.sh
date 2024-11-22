#!/bin/bash

# Configurações do Kafka
BROKER="kafka:9092"
TOPICS=("payment-initiated" "payment-fraud-checked" "settlement-completed")
KAFKA_TOPICS_CMD="/usr/bin/kafka-topics" # Ajuste o caminho, se necessário

# Verifica se o comando kafka-topics está disponível
if ! command -v $KAFKA_TOPICS_CMD &> /dev/null; then
  echo "Erro: kafka-topics.sh não está instalado ou no PATH."
  exit 1
fi

# Aguarda o Kafka estar disponível
MAX_RETRIES=60
RETRY_COUNT=0

echo "Aguardando Kafka estar acessível em $BROKER..."
while ! nc -z $(echo $BROKER | cut -d':' -f1) $(echo $BROKER | cut -d':' -f2); do
  sleep 1
  RETRY_COUNT=$((RETRY_COUNT + 1))
  if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
    echo "Erro: Kafka não está acessível após $MAX_RETRIES tentativas."
    exit 1
  fi
done

# Criação dos tópicos
for TOPIC in "${TOPICS[@]}"; do
  echo "Criando o tópico: $TOPIC"
  $KAFKA_TOPICS_CMD --create --if-not-exists \
    --bootstrap-server "$BROKER" \
    --replication-factor 1 \
    --partitions 1 \
    --topic "$TOPIC"
done

echo "Todos os tópicos foram criados com sucesso."
