package com.example.crm.service;

import com.example.crm.dao.SaleDAO;
import com.example.crm.model.Sale;

import java.sql.SQLException;
import java.util.List;

public class SaleService {
    private final SaleDAO dao = new SaleDAO();

    public Sale createSale(Sale s) throws ServiceException {
        try {
            if (s.getAmount() <= 0) throw new IllegalArgumentException("Amount must be positive");
            return dao.create(s);
        } catch (SQLException | IllegalArgumentException e) {
            throw new ServiceException("Failed to create sale: " + e.getMessage(), e);
        }
    }

    public List<Sale> listSales() throws ServiceException {
        try { return dao.findAll(); } catch (SQLException e) { throw new ServiceException("Failed to list sales", e); }
    }
}
