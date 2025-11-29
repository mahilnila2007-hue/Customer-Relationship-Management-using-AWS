package com.example.crm.service;

import com.example.crm.dao.LeadDAO;
import com.example.crm.model.Lead;

import java.sql.SQLException;
import java.util.List;

public class LeadService {
    private final LeadDAO dao = new LeadDAO();

    public Lead createLead(Lead l) throws ServiceException {
        try {
            if (l.getName() == null || l.getName().isBlank()) throw new IllegalArgumentException("Lead name required");
            return dao.create(l);
        } catch (SQLException | IllegalArgumentException e) {
            throw new ServiceException("Failed to create lead: " + e.getMessage(), e);
        }
    }

    public List<Lead> listLeads() throws ServiceException {
        try { return dao.findAll(); } catch (SQLException e) { throw new ServiceException("Failed to list leads", e); }
    }
}
