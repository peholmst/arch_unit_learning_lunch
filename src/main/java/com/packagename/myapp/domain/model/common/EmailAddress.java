package com.packagename.myapp.domain.model.common;

import com.packagename.myapp.domain.model.ValueObject;
import org.springframework.lang.NonNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class EmailAddress implements ValueObject {

    private final String emailAddress;

    private EmailAddress(@NonNull String emailAddress) {
        this.emailAddress = requireNonNull(emailAddress);
    }

    @NonNull
    public static EmailAddress fromString(@NonNull String emailAddress) {
        return new EmailAddress(emailAddress);
    }

    @Override
    public String toString() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAddress that = (EmailAddress) o;
        return emailAddress.equals(that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }
}
