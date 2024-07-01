package com.fullstackapp.ecommerce.dto;

//to send back a java object as a JSON

import lombok.Data;

@Data  //will generate constructor only for final fields
public class PurchaseResponse {
    private final String orderTrackingNumber;
}
