package com.fullstackapp.ecommerce.dao;

import com.fullstackapp.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

//Customer is Entity Type and Long is primary key type
//Customer has a collection of orders
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //SELECT * from Customer c WHERE c.email = theEmail, using this internally
    Customer findByEmail(String email);
}
