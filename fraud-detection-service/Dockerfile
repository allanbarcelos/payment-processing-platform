# Use uma imagem base do OpenJDK com suporte a Scala
FROM openjdk:17-slim

# Instalar ferramentas necessárias
RUN apt-get update && apt-get install -y \
    curl \
    tar \
    && rm -rf /var/lib/apt/lists/*

# Diretório de trabalho no container
WORKDIR /app

# Baixar e instalar o SBT
RUN curl -L https://github.com/sbt/sbt/releases/download/v1.9.0/sbt-1.9.0.tgz | tar -xz -C /usr/local/bin

# Configurar o SBT para ser acessível no PATH
ENV PATH="/usr/local/bin/sbt/bin:$PATH"

# Copiar os arquivos do projeto
COPY . .

# Compilar o projeto com SBT
RUN sbt clean assembly

# Comando para rodar a aplicação
CMD ["java", "-jar", "target/scala-2.13/fraud-detection-service-assembly-0.1.jar"]
