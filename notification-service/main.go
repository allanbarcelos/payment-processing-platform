package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	log.Println("Starting Notification Service...")

	// Inicializa o consumidor Kafka
	consumer, err := NewKafkaConsumer("payment-completed", "notification-group")
	if err != nil {
		log.Fatalf("Failed to create Kafka consumer: %v", err)
	}

	// Canal para parar o serviço com sinal de interrupção (Ctrl+C)
	stopChan := make(chan os.Signal, 1)
	signal.Notify(stopChan, syscall.SIGINT, syscall.SIGTERM)

	// Inicia o consumo de mensagens
	go func() {
		if err := consumer.Start(HandleNotification); err != nil {
			log.Fatalf("Error consuming Kafka messages: %v", err)
		}
	}()

	log.Println("Notification Service is running. Waiting for messages...")
	<-stopChan // Aguarda interrupção
	log.Println("Shutting down Notification Service...")
}
