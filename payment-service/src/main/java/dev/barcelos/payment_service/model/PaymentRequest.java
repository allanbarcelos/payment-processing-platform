package dev.barcelos.payment_service.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    
    @NotBlank(message = "User ID is required.")
    private String userId;

    @NotNull(message = "Amount is required.")
    @Min(value = 1, message = "Amount must be greater than 0.")
    private Double amount;

    @NotBlank(message = "Currency is required.")
    private String currency;

    @NotNull(message = "Payment Method is required.")
    private PaymentMethod paymentMethod;
}

