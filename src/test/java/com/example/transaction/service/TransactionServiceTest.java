package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import com.razorpay.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RazorpayClientWrapper razorpayClientWrapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testCreateOrder_success() throws Exception {
        Order mockOrder = mock(Order.class);
        when(mockOrder.get("id")).thenReturn("order_xyz");

        when(razorpayClientWrapper.createOrder(anyInt(), anyString())).thenReturn(mockOrder);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        
        Transaction saved = transactionService.createOrder(500, "INR");

        assertEquals("order_xyz", saved.getOrderId());
        assertEquals("CREATED", saved.getStatus());
    }

    @Test
    public void testConfirmPayment_success() {
        Transaction t = new Transaction();
        t.setOrderId("order123");

        when(transactionRepository.findByOrderId("order123")).thenReturn(Optional.of(t));

        transactionService.confirmPayment("order123", "pay_456");

        assertEquals("pay_456", t.getPaymentId());
        assertEquals("PAID", t.getStatus());
        
    }
}