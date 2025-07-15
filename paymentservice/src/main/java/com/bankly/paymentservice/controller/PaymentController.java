package com.bankly.paymentservice.controller;


import com.bankly.paymentservice.dto.PaymentValidationRequest;
import com.bankly.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validatePayment(@RequestBody PaymentValidationRequest request) {
        // In a no-security setup, we need a dummy user ID for business logic.
        // This userId would typically be propagated from the Transaction Service.


        boolean isValid = paymentService.validatePayment(request, request.getUserName());
        return ResponseEntity.ok(isValid);
    }


}
