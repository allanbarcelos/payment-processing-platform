package dev.barcelos.payment_service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;



@Data
@Entity
public class Payment {
    @Id
    private String id;

    @JsonProperty("user_id")
    private String userId;

    private Double amount;
    
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PaymentMethod paymentMethod;
    
    private String status; // Status gerenciado pelo servidor (PENDING, COMPLETED, etc.)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
