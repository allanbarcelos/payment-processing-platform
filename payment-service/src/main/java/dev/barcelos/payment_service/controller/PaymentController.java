package dev.barcelos.payment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.barcelos.payment_service.dto.ApiResponse;
import dev.barcelos.payment_service.model.PaymentRequest;
import dev.barcelos.payment_service.model.Payment;
import dev.barcelos.payment_service.repository.PaymentRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final String TOPIC = "payment-initiated";

    @PostMapping
    public ResponseEntity<ApiResponse<String>> initiatePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        try {
            // Criar o pagamento com status inicial
            Payment payment = new Payment();
            payment.setId(UUID.randomUUID().toString());
            payment.setUserId(paymentRequest.getUserId());
            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency());
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setStatus("PENDING"); // Define o status inicial
            payment.setCreatedAt(LocalDateTime.now());

            // Salvar o pagamento no banco de dados
            paymentRepository.save(payment);

            // Serializar e enviar a mensagem para o Kafka
            String paymentMessage = objectMapper.writeValueAsString(payment);
            kafkaTemplate.send(TOPIC, paymentMessage);

            // Retorna uma resposta JSON com o ID do pagamento
            return ResponseEntity.ok(new ApiResponse<>("Pagamento iniciado com sucesso.", payment.getId()));
        } catch (DataAccessException e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Erro ao acessar o banco de dados.", null));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Erro ao serializar o pagamento.", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Erro desconhecido ao iniciar pagamento.", null));
        }
    }
}
