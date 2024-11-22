# Use uma imagem base do Rust
FROM rust:1.73-slim

# Instalar dependências necessárias para compilação
RUN apt-get update && apt-get install -y \
    build-essential \
    g++ \
    pkg-config \
    zlib1g-dev \
    python3 \
    && rm -rf /var/lib/apt/lists/*

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo Cargo.toml e o lock para o cache
COPY Cargo.* ./

# Baixa dependências do Cargo para otimizar o cache
RUN cargo fetch

# Copia o código fonte
COPY . .

# Compila o projeto
RUN cargo build --release

# Define o comando de execução
CMD ["./target/release/settlement-service"]
