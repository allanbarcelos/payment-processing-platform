use rdkafka::consumer::{Consumer, StreamConsumer};
use rdkafka::error::KafkaResult;
use rdkafka::message::Message;
use rdkafka::producer::{BaseProducer, BaseRecord, Producer}; // Importa a trait Producer
use rdkafka::config::FromClientConfig; // Importa a trait necessária
use rdkafka::ClientConfig;
use std::time::Duration;

pub async fn consume_messages<F>(topic: &str, group_id: &str, handler: F) -> KafkaResult<()>
where
    F: Fn(&str) + Send + Sync + 'static,
{
    let consumer: StreamConsumer = ClientConfig::new()
        .set("group.id", group_id)
        .set("bootstrap.servers", "kafka:9092")
        .set("enable.auto.commit", "true")
        .create()?;

    consumer.subscribe(&[topic])?;

    log::info!("Listening for messages on topic: {}", topic);

    while let Ok(message) = consumer.recv().await {
        // Agora trata o Result corretamente
        if let Some(payload) = message.payload_view::<str>() {
            if let Ok(payload) = payload {
                log::info!("Received message: {}", payload);
                handler(payload);
            }
        }
    }

    Ok(())
}

pub fn produce_message(topic: &str, message: &str) -> KafkaResult<()> {
    let producer = BaseProducer::from_config(
        &ClientConfig::new()
            .set("bootstrap.servers", "kafka:9092"),
    )?; // Corrige o uso de `from_config`

    // Converte o erro para `KafkaError`
    producer.send(
        BaseRecord::to(topic)
            .payload(message)
            .key("key"),
    ).map_err(|(e, _)| e)?; // Trata o erro explicitamente

    producer.flush(Duration::from_secs(1)); // Agora flush está acessível
    log::info!("Produced message to topic {}: {}", topic, message);
    Ok(())
}
