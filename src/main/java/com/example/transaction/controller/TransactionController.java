package com.example.transaction.controller;

import com.example.transaction.model.Transaction;
import com.example.transaction.service.TransactionService;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Transaction> createOrder(@RequestParam int amount, @RequestParam String currency) throws RazorpayException {
        return ResponseEntity.ok(transactionService.createOrder(amount, currency));
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestParam String orderId, @RequestParam String paymentId) {
        transactionService.confirmPayment(orderId, paymentId);
        return ResponseEntity.ok("Payment confirmed");
    }
}