# Usar a imagem base do Golang
FROM golang:1.20

# Diretório de trabalho no container
WORKDIR /app

# Copiar os arquivos para o container
COPY go.mod go.sum ./
RUN go mod download

COPY . ./

# Compilar o binário Go
RUN go build -o notification-service

# Comando de inicialização
CMD ["./notification-service"]
