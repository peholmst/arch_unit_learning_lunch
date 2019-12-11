package com.packagename.myapp.domain.model;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface Repository<T extends AggregateRoot<ID>, ID extends AggregateRootId>
        extends JpaRepository<T, String>, QuerydslPredicateExecutor<T> {

    @NonNull
    default Optional<T> findById(@NonNull ID id) {
        return findById(id.toString());
    }

    @NonNull
    default T getById(@NonNull ID id) {
        return findById(id).orElseThrow(() -> new DataRetrievalFailureException("No such aggregate found"));
    }
}
