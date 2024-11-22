package main

import (
	"context"
	"log"

	"github.com/segmentio/kafka-go"
)

// HandleNotification processa a notificação
func HandleNotification(ctx context.Context, msg kafka.Message) error {
	log.Printf("Processing notification: %s", string(msg.Value))
	// Aqui você pode implementar a lógica de envio de notificações
	return nil
}
