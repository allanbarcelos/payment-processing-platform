# Use a imagem base do OpenJDK 21
FROM openjdk:21-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR gerado pelo Maven para o diretório de trabalho
COPY target/payment-service-1.0-SNAPSHOT.jar payment-service.jar

# Define a porta na qual o serviço será exposto
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "payment-service.jar"]
