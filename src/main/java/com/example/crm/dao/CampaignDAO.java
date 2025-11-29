package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.Campaign;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampaignDAO {
    public Campaign create(Campaign c) throws SQLException {
        String sql = "INSERT INTO campaigns(name,channel,start_date,end_date,budget,notes) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName()); ps.setString(2, c.getChannel()); ps.setString(3, c.getStartDate()); ps.setString(4, c.getEndDate()); ps.setDouble(5, c.getBudget()); ps.setString(6, c.getNotes());
            ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) c.setId(rs.getInt(1)); }
        }
        return c;
    }

    public List<Campaign> findAll() throws SQLException {
        List<Campaign> list = new ArrayList<>();
        String sql = "SELECT id,name,channel,start_date,end_date,budget,notes FROM campaigns";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Campaign c = new Campaign();
                c.setId(rs.getInt("id")); c.setName(rs.getString("name")); c.setChannel(rs.getString("channel")); c.setStartDate(rs.getString("start_date"));
                c.setEndDate(rs.getString("end_date")); c.setBudget(rs.getDouble("budget")); c.setNotes(rs.getString("notes"));
                list.add(c);
            }
        }
        return list;
    }
}
