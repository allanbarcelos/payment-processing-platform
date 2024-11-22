use serde::{Deserialize, Serialize};
use serde_json::Result;
use serde::ser::Error; // Importa a trait necessária
use chrono::Utc; // Importa chrono para manipulação de datas

#[derive(Serialize, Deserialize, Debug)]
pub struct Payment {
    user_id: String,
    amount: f64,
    status: String,
}

#[derive(Serialize, Deserialize, Debug)]
pub struct SettledPayment {
    user_id: String,
    amount: f64,
    status: String,
    settled_at: String,
}

pub fn process_settlement(message: &str) -> Result<String> {
    // Decodifica a mensagem Kafka para Payment
    let payment: Payment = serde_json::from_str(message)?;

    log::info!("Processing payment for user: {}", payment.user_id);

    // Simula lógica de liquidação
    if payment.status == "valid" {
        let settled_payment = SettledPayment {
            user_id: payment.user_id,
            amount: payment.amount,
            status: "settled".to_string(),
            settled_at: Utc::now().to_rfc3339(), // Corrige o uso de chrono
        };

        // Retorna o pagamento liquidado como JSON
        let result = serde_json::to_string(&settled_payment)?;
        Ok(result)
    } else {
        log::warn!(
            "Payment for user {} is not valid and will not be settled.",
            payment.user_id
        );
        Err(Error::custom("Invalid payment status")) // Corrige o uso de Error
    }
}
