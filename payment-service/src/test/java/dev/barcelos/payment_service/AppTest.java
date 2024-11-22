package dev.barcelos.payment_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.barcelos.payment_service.controller.PaymentController;
import dev.barcelos.payment_service.model.Payment;
import dev.barcelos.payment_service.model.PaymentMethod;
import dev.barcelos.payment_service.model.PaymentRequest;
import dev.barcelos.payment_service.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

@WebMvcTest(PaymentController.class)
public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    public void testPaymentSerialization() throws JsonProcessingException {
        Payment payment = new Payment();
        payment.setId("123");
        payment.setUserId("user123");
        payment.setAmount(100.0);
        payment.setCurrency("USD");
        payment.setPaymentMethod(PaymentMethod.CASH);
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());

        String json = objectMapper.writeValueAsString(payment);

        assertNotNull(json);
    }

    @Test
    public void testInitiatePayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setUserId("user123");
        request.setAmount(200.50);
        request.setCurrency("USD");
        request.setPaymentMethod(PaymentMethod.CASH);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(Mockito.eq("payment-initiated"), messageCaptor.capture());

        String sentMessage = messageCaptor.getValue();
        Payment sentPayment = objectMapper.readValue(sentMessage, Payment.class);

        assertEquals(request.getUserId(), sentPayment.getUserId());
        assertEquals(request.getAmount(), sentPayment.getAmount());
        assertEquals(request.getCurrency(), sentPayment.getCurrency());
        assertEquals(request.getPaymentMethod(), sentPayment.getPaymentMethod());
    }

    @Test
    public void testInitiatePaymentWithInvalidInput() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid-json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInitiatePaymentWithEmptyBody() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("User ID is required")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Amount is required.")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Currency is required")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Payment Method is required")));
    }

    @Test
    public void testInitiatePaymentWithAmountLessThan1() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("User ID is required")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Amount must be greater than 0.")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Currency is required")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Payment Method is required")));
    }
}
