package com.example.crm.service;

import com.example.crm.dao.InteractionDAO;
import com.example.crm.model.Interaction;

import java.sql.SQLException;
import java.util.List;

public class InteractionService {
    private final InteractionDAO dao = new InteractionDAO();

    public Interaction createInteraction(Interaction i) throws ServiceException { try { if (i.getCustomerId()==null) throw new IllegalArgumentException("customer required"); return dao.create(i); } catch (SQLException|IllegalArgumentException e){ throw new ServiceException("Create interaction failed", e); } }

    public List<Interaction> listByCustomer(int customerId) throws ServiceException { try { return dao.findAllByCustomer(customerId); } catch (SQLException e){ throw new ServiceException("List interactions failed", e); } }
}
