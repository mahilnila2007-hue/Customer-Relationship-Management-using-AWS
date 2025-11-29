package com.example.crm.service;

import com.example.crm.dao.CampaignDAO;
import com.example.crm.model.Campaign;

import java.sql.SQLException;
import java.util.List;

public class CampaignService {
    private final CampaignDAO dao = new CampaignDAO();

    public Campaign createCampaign(Campaign c) throws ServiceException {
        try { if (c.getName() == null || c.getName().isBlank()) throw new IllegalArgumentException("Name required"); return dao.create(c); }
        catch (SQLException|IllegalArgumentException e) { throw new ServiceException("Create campaign failed", e); }
    }

    public List<Campaign> listCampaigns() throws ServiceException { try { return dao.findAll(); } catch (SQLException e) { throw new ServiceException("List campaigns failed", e); } }
}
