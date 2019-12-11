package com.packagename.myapp.domain.model.common;

import com.packagename.myapp.domain.model.customer.CustomerId;

// The only purpose of this class is to introduce a cycle that ArchUnit can detect
public class SomeUtilClass {

    public static CustomerId callCustomerModule() {
        return CustomerId.random();
    }
}
