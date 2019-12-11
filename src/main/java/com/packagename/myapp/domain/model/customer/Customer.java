package com.packagename.myapp.domain.model.customer;

import com.packagename.myapp.domain.model.AggregateRoot;
import com.packagename.myapp.domain.model.common.EmailAddress;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static java.util.Objects.requireNonNull;

@Entity
@Table
public class Customer extends AggregateRoot<CustomerId> {

    @Column(nullable = false)
    private String name;

    //    @Convert(converter = EmailAddressConverter.class)
    @Column
    private EmailAddress email;

    @SuppressWarnings("unused")
    protected Customer() { // Used by JPA only
    }

    public Customer(@NonNull CustomerId id, @NonNull String name) {
        super(id);
        this.name = requireNonNull(name);
    }

    @Override
    protected CustomerId wrapId(@NonNull String id) {
        return CustomerId.fromString(id);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public EmailAddress getEmail() {
        return email;
    }

    public void setEmail(@Nullable EmailAddress email) {
        this.email = email;
    }
}
