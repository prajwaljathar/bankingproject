package com.example.transaction.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RazorpayClientWrapper {
    private final RazorpayClient razorpayClient;

    public RazorpayClientWrapper(@Value("${razorpay.key_id}") String key,
                                 @Value("${razorpay.key_secret}") String secret) throws RazorpayException {
        this.razorpayClient = new RazorpayClient(key, secret);
    }

    public Order createOrder(int amount, String currency) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", UUID.randomUUID().toString());
        return razorpayClient.orders.create(orderRequest);
    }
}