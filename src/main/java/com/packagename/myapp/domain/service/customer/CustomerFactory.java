package com.packagename.myapp.domain.service.customer;

import com.packagename.myapp.domain.model.customer.Customer;
import com.packagename.myapp.domain.model.customer.CustomerId;
import com.packagename.myapp.domain.service.DomainService;
import org.springframework.lang.NonNull;

@DomainService
public class CustomerFactory {

    @NonNull
    public Customer createCustomer(@NonNull String customerName) {
        return new Customer(CustomerId.random(), customerName);
    }
}
