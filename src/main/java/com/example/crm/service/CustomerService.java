package com.example.crm.service;

import com.example.crm.dao.CustomerDAO;
import com.example.crm.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final CustomerDAO dao = new CustomerDAO();

    public Customer createCustomer(Customer c) throws ServiceException {
        try {
            validate(c);
            return dao.create(c);
        } catch (SQLException | IllegalArgumentException e) {
            throw new ServiceException("Failed to create customer: " + e.getMessage(), e);
        }
    }

    public List<Customer> listCustomers() throws ServiceException {
        try { return dao.findAll(); } catch (SQLException e) { throw new ServiceException("Failed to list customers", e); }
    }

    public boolean deleteCustomer(int id) throws ServiceException {
        try { return dao.delete(id); } catch (SQLException e) { throw new ServiceException("Failed to delete customer", e); }
    }

    private void validate(Customer c) {
        if (c == null) throw new IllegalArgumentException("Customer cannot be null");
        if (c.getName() == null || c.getName().isBlank()) throw new IllegalArgumentException("Customer name required");
    }
}
