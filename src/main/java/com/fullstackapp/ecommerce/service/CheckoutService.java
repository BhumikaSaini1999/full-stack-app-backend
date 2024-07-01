package com.fullstackapp.ecommerce.service;

import com.fullstackapp.ecommerce.dto.PaymentInfo;
import com.fullstackapp.ecommerce.dto.Purchase;
import com.fullstackapp.ecommerce.dto.PurchaseResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

public interface CheckoutService {
    PurchaseResponse placeOrder(Purchase purchase);

    Order createOrder(PaymentInfo paymentInfo) throws RazorpayException;
}
