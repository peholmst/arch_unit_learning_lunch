package com.packagename.myapp.application.customer;

import com.packagename.myapp.domain.model.common.EmailAddress;
import com.packagename.myapp.domain.model.customer.CustomerId;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CustomerService {

    @NonNull
    List<CustomerListingDTO> findCustomers();

    @NonNull
    CustomerId createCustomer(@NonNull String customerName);

    void changeCustomerEmail(@NonNull CustomerId customerId, @NonNull EmailAddress newEmailAddress);
}
