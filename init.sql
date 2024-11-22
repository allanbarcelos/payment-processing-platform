-- Criação do tipo ENUM para método de pagamento
CREATE TYPE paymentMethod AS ENUM (
    'CREDIT_CARD',
    'DEBIT_CARD',
    'PAYPAL',
    'BANK_TRANSFER',
    'CASH'
);

-- Criação da tabela de pagamentos
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,                    -- Identificador único
    user_id VARCHAR(255) NOT NULL,           -- Identificador do usuário
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0), -- Valor do pagamento, deve ser maior que 0
    currency CHAR(3) NOT NULL,               -- Código da moeda (ISO 4217)
    paymentMethod paymentMethod NOT NULL,    -- Método de pagamento (ENUM)
    status VARCHAR(50) NOT NULL,             -- Status do pagamento
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data e hora da criação
);
