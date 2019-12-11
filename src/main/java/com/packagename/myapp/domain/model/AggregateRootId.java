package com.packagename.myapp.domain.model;

import org.springframework.lang.NonNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class AggregateRootId implements ValueObject {

    private final String uuid;

    protected AggregateRootId(@NonNull String uuid) {
        this.uuid = requireNonNull(uuid);
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregateRootId that = (AggregateRootId) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
