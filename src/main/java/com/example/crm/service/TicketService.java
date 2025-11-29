package com.example.crm.service;

import com.example.crm.dao.TicketDAO;
import com.example.crm.model.Ticket;

import java.sql.SQLException;
import java.util.List;

public class TicketService {
    private final TicketDAO dao = new TicketDAO();

    public Ticket createTicket(Ticket t) throws ServiceException { try { if (t.getSubject()==null||t.getSubject().isBlank()) throw new IllegalArgumentException("subject required"); return dao.create(t); } catch (SQLException|IllegalArgumentException e){ throw new ServiceException("Create ticket failed", e); } }

    public List<Ticket> listTickets() throws ServiceException { try { return dao.findAll(); } catch (SQLException e){ throw new ServiceException("List tickets failed", e); } }
}
