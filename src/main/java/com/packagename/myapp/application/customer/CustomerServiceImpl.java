package com.packagename.myapp.application.customer;

import com.packagename.myapp.application.ApplicationService;
import com.packagename.myapp.domain.model.common.EmailAddress;
import com.packagename.myapp.domain.model.customer.Customer;
import com.packagename.myapp.domain.model.customer.CustomerId;
import com.packagename.myapp.domain.model.customer.CustomerRepository;
import com.packagename.myapp.domain.service.customer.CustomerFactory;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.packagename.myapp.domain.model.customer.QCustomer.customer;
import static java.util.Objects.requireNonNull;

@ApplicationService
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CustomerServiceImpl implements CustomerService {

    // Given the structure of Customer, CustomerListingDTO and this application service, the design is way
    // over-engineered but that is not the point. The point is to show how ArchUnit can be used to enforce a certain
    // architecture.

    private final EntityManager entityManager;
    private final CustomerRepository customerRepository;
    private final CustomerFactory customerFactory;

    CustomerServiceImpl(EntityManager entityManager, CustomerRepository customerRepository, CustomerFactory customerFactory) {
        this.entityManager = entityManager;
        this.customerRepository = customerRepository;
        this.customerFactory = customerFactory;
    }

    @Override
    // @NonNull
    public List<CustomerListingDTO> findCustomers() {
        return new JPAQuery<>(entityManager)
                .select(customer.id, customer.name, customer.email)
                .from(customer)
                .orderBy(customer.name.asc())
                .fetch()
                .stream()
                .map(row -> CustomerListingDTO.builder()
                        .id(CustomerId.fromString(requireNonNull(row.get(customer.id))))
                        .name(row.get(customer.name))
                        .emailAddress(row.get(customer.email))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public CustomerId createCustomer(@NonNull String customerName) {
        return customerRepository.saveAndFlush(customerFactory.createCustomer(customerName)).getId();
    }

    @Override
    public void changeCustomerEmail(@NonNull CustomerId customerId, @NonNull EmailAddress newEmailAddress) {
        Customer customer = customerRepository.getById(customerId);
        customer.setEmail(requireNonNull(newEmailAddress));
        customerRepository.saveAndFlush(customer);
    }
}
