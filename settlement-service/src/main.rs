mod kafka;
mod settlement;

use kafka::{consume_messages, produce_message};
use settlement::process_settlement;
use tokio::signal;
use std::process;

#[tokio::main]
async fn main() {
    // Configuração de logger
    env_logger::init();

    // Tópicos Kafka
    let input_topic = "payment-fraud-checked";
    let output_topic = "payment-settled";
    let group_id = "settlement-service-group";

    log::info!("Settlement Service is starting...");

    // Consome mensagens de Kafka
    if let Err(e) = consume_messages(input_topic, group_id, |msg| {
        let result = process_settlement(msg);
        match result {
            Ok(settled_payment) => {
                if let Err(err) = produce_message(output_topic, &settled_payment) {
                    log::error!("Failed to produce message: {}", err);
                }
            }
            Err(err) => {
                log::error!("Failed to process settlement: {}", err);
            }
        }
    })
    .await
    {
        log::error!("Error in Settlement Service: {}", e);
        process::exit(1);
    }

    // Aguarda sinal de encerramento
    signal::ctrl_c().await.unwrap();
    log::info!("Settlement Service is shutting down...");
}
