package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final RazorpayClientWrapper razorpayClientWrapper;

    public TransactionService(TransactionRepository transactionRepository, RazorpayClientWrapper razorpayClientWrapper) {
        this.transactionRepository = transactionRepository;
        this.razorpayClientWrapper = razorpayClientWrapper;
    }

    public Transaction createOrder(int amount, String currency) throws RazorpayException {
        Order order = razorpayClientWrapper.createOrder(amount, currency);
        Transaction transaction = new Transaction();
        transaction.setOrderId(order.get("id"));
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setStatus("CREATED");
        return transactionRepository.save(transaction);
    }

    public void confirmPayment(String orderId, String paymentId) {
        Optional<Transaction> transaction = transactionRepository.findByOrderId(orderId);
        if (transaction.isPresent()) {
            Transaction t = transaction.get();
            t.setPaymentId(paymentId);
            t.setStatus("PAID");
            transactionRepository.save(t);
        } 
    }
}