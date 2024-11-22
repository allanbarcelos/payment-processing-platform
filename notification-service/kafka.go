package main

import (
	"context"
	"log"
	"time"

	"github.com/segmentio/kafka-go"
)

// KafkaConsumer representa o consumidor Kafka
type KafkaConsumer struct {
	Reader *kafka.Reader
}

// NewKafkaConsumer cria um novo consumidor Kafka
func NewKafkaConsumer(topic, groupID string) (*KafkaConsumer, error) {
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:  []string{"kafka:9092"},
		Topic:    topic,
		GroupID:  groupID,
		MinBytes: 10e3, // 10KB
		MaxBytes: 10e6, // 10MB
	})
	return &KafkaConsumer{Reader: reader}, nil
}

// Start inicia o consumo de mensagens
func (kc *KafkaConsumer) Start(handler func(context.Context, kafka.Message) error) error {
	defer kc.Reader.Close()
	ctx := context.Background()

	for {
		msg, err := kc.Reader.ReadMessage(ctx)
		if err != nil {
			log.Printf("Error reading message: %v", err)
			time.Sleep(2 * time.Second)
			continue
		}

		log.Printf("Received message: key=%s value=%s", string(msg.Key), string(msg.Value))
		if err := handler(ctx, msg); err != nil {
			log.Printf("Error handling message: %v", err)
		}
	}
}
