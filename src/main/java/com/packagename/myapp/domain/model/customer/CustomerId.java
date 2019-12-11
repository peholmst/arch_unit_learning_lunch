package com.packagename.myapp.domain.model.customer;

import com.packagename.myapp.domain.model.AggregateRootId;
import org.springframework.lang.NonNull;

import java.util.UUID;

public class CustomerId extends AggregateRootId {

    private CustomerId(@NonNull String uuid) {
        super(uuid);
    }

    @NonNull
    public static CustomerId random() {
        return new CustomerId(UUID.randomUUID().toString());
    }

    @NonNull
    public static CustomerId fromString(@NonNull String customerId) {
        return new CustomerId(customerId);
    }
}
