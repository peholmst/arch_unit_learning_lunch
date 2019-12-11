package com.packagename.myapp.application.customer;

import com.packagename.myapp.domain.model.common.EmailAddress;
import com.packagename.myapp.domain.model.customer.CustomerId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerListingDTO {
    private final CustomerId id;
    private final String name;
    private EmailAddress emailAddress;
}
