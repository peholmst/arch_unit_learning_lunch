package com.packagename.myapp.ui;

import com.packagename.myapp.application.customer.CustomerListingDTO;
import com.packagename.myapp.application.customer.CustomerService;
import com.packagename.myapp.domain.model.common.EmailAddress;
import com.packagename.myapp.domain.model.customer.Customer;
import com.packagename.myapp.domain.model.customer.CustomerRepository;
import com.packagename.myapp.domain.service.customer.CustomerFactory;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Objects;

@Route
public class MainView extends VerticalLayout {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final CustomerFactory customerFactory;
    private Grid<CustomerListingDTO> customerGrid;

    public MainView(CustomerService customerService, CustomerRepository customerRepository, CustomerFactory customerFactory) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.customerFactory = customerFactory;

        setSizeFull();

        customerGrid = new Grid<>();
        customerGrid.addColumn(CustomerListingDTO::getId).setHeader("Customer ID");
        customerGrid.addColumn(CustomerListingDTO::getName).setHeader("Customer Name");
        customerGrid.addColumn(CustomerListingDTO::getEmailAddress).setHeader("E-mail Address");
        customerGrid.setSizeFull();

        add(customerGrid);

        Button createCustomer = new Button("Create Customer",
                event -> createCustomer());
        Button changeEmailAddress = new Button("Change E-mail Address",
                event -> customerGrid.getSelectionModel().getFirstSelectedItem().ifPresent(this::changeEmailAddress));
        changeEmailAddress.setEnabled(false);
        Button refresh = new Button("Refresh", event -> refresh());

        customerGrid.getSelectionModel().addSelectionListener(event -> changeEmailAddress.setEnabled(event.getFirstSelectedItem().isPresent()));

        HorizontalLayout buttons = new HorizontalLayout(createCustomer, changeEmailAddress, refresh);
        add(buttons);

        refresh();
    }

    private void refresh() {
        // customerGrid.setDataProvider(DataProvider.ofCollection(customerService.findCustomers()));
        customerGrid.setDataProvider(DataProvider.fromStream(customerRepository.findAll()
                .stream()
                .map(c -> CustomerListingDTO.builder().id(c.getId()).name(c.getName()).emailAddress(c.getEmail()).build())));
    }

    private void createCustomer() {
        Dialog dialog = new Dialog();

        TextField customerName = new TextField("Customer Name");
        customerName.focus();
        dialog.add(customerName);

        Button create = new Button("Create", event -> {
            //customerService.createCustomer(customerName.getValue());
            Customer customer = customerFactory.createCustomer(customerName.getValue());
            customerRepository.saveAndFlush(customer);
            dialog.close();
            refresh();
        });
        create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        create.addClickShortcut(Key.ENTER);

        Button cancel = new Button("Cancel", event -> dialog.close());
        dialog.add(new HorizontalLayout(cancel, create));
        dialog.open();
    }

    private void changeEmailAddress(CustomerListingDTO dto) {
        Dialog dialog = new Dialog();

        TextField customerName = new TextField("Customer Name");
        customerName.setValue(dto.getName());
        customerName.setReadOnly(true);

        TextField emailAddress = new TextField("Email Address");
        emailAddress.setValue(Objects.toString(dto.getEmailAddress(), ""));
        emailAddress.focus();

        Button change = new Button("Change", event -> {
            EmailAddress newEmailAddress = EmailAddress.fromString(emailAddress.getValue());
            customerService.changeCustomerEmail(dto.getId(), newEmailAddress);
            dialog.close();
            refresh();
        });
        change.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        change.addClickShortcut(Key.ENTER);

        Button cancel = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(customerName, emailAddress));
        dialog.add(new HorizontalLayout(cancel, change));

        emailAddress.addValueChangeListener(event -> change.setEnabled(!emailAddress.isEmpty()));
        change.setEnabled(!emailAddress.isEmpty());

        dialog.open();
    }
}
