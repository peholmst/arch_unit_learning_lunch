package com.packagename.myapp.domain.model;

import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.NonNull;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@MappedSuperclass
public abstract class AggregateRoot<ID extends AggregateRootId> implements Serializable {

    @Id
    private String id;

    @Version
    private Long version;

    protected AggregateRoot() {
    }

    protected AggregateRoot(@NonNull ID id) {
        this.id = requireNonNull(id).toString();
    }

    @NonNull
    public ID getId() {
        return wrapId(id);
    }

    @NonNull
    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }

    public boolean isNew() {
        return version == null;
    }

    protected abstract ID wrapId(@NonNull String id);

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") // Yes it does, IntelliJ just doesn't understand it
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !this.getClass().equals(ProxyUtils.getUserClass(o))) return false;
        AggregateRoot<?> that = (AggregateRoot<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
