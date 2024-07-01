package com.fullstackapp.ecommerce.service;

import com.fullstackapp.ecommerce.dao.CustomerRepository;
import com.fullstackapp.ecommerce.dto.PaymentInfo;
import com.fullstackapp.ecommerce.dto.Purchase;
import com.fullstackapp.ecommerce.dto.PurchaseResponse;
import com.fullstackapp.ecommerce.entity.Customer;
import com.fullstackapp.ecommerce.entity.Order;
import com.fullstackapp.ecommerce.entity.OrderItem;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;
    private RazorpayClient razorpayClient;

    @Autowired //optional we have only one constructor
    public CheckoutServiceImpl(CustomerRepository customerRepository, RazorpayClient razorpayClient) {
        this.customerRepository = customerRepository;
        this.razorpayClient = razorpayClient;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // retrieve the order info from dto
        Order order = purchase.getOrder();

        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        //populate order with order items
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item-> order.add(item));

        //populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();

        //check if this is an existing customer to ensure unique entry in customer table for unique email
        String theEmail = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(theEmail);
        if(customerFromDB!=null){
            customer = customerFromDB;
        }

        customer.add(order);

        //save to the database
        customerRepository.save(customer);

        //return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public com.razorpay.Order createOrder(PaymentInfo paymentInfo) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", paymentInfo.getAmount());
        orderRequest.put("currency", paymentInfo.getCurrency());
        return razorpayClient.orders.create(orderRequest);
    }

    private String generateOrderTrackingNumber() {
        //generate a random UUID number (Universally unique identifier- Version4)
        return UUID.randomUUID().toString();
    }
}
