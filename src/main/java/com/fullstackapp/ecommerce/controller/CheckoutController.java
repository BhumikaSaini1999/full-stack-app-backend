package com.fullstackapp.ecommerce.controller;

import com.fullstackapp.ecommerce.dto.PaymentInfo;
import com.fullstackapp.ecommerce.dto.Purchase;
import com.fullstackapp.ecommerce.dto.PurchaseResponse;
import com.fullstackapp.ecommerce.service.CheckoutService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private Logger logger =  Logger.getLogger(CheckoutController.class.getName());
    private CheckoutService checkoutService;

    @Autowired//optional for single constructor
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase){
        PurchaseResponse purchaseResponse = checkoutService.placeOrder(purchase);
        return purchaseResponse;
    }

    @PostMapping("/payment-order")
    public ResponseEntity<String> createOrder(@RequestBody PaymentInfo paymentInfo) throws RazorpayException {
        logger.info("paymentinfo.amount: " + paymentInfo.getAmount());
        Order order = checkoutService.createOrder(paymentInfo);
        return new ResponseEntity<>(order.toString(), HttpStatus.OK);
    }
}
