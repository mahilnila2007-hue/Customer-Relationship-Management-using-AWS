package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    public Ticket create(Ticket t) throws SQLException {
        String sql = "INSERT INTO tickets(customer_id,subject,description,status,priority,created_at,updated_at) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, t.getCustomerId()); ps.setString(2, t.getSubject()); ps.setString(3, t.getDescription()); ps.setString(4, t.getStatus()); ps.setString(5, t.getPriority()); ps.setString(6, t.getCreatedAt()); ps.setString(7, t.getUpdatedAt());
            ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) t.setId(rs.getInt(1)); }
        }
        return t;
    }

    public List<Ticket> findAll() throws SQLException {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT id,customer_id,subject,description,status,priority,created_at,updated_at FROM tickets";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setId(rs.getInt("id")); t.setCustomerId(rs.getInt("customer_id")); t.setSubject(rs.getString("subject")); t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status")); t.setPriority(rs.getString("priority")); t.setCreatedAt(rs.getString("created_at")); t.setUpdatedAt(rs.getString("updated_at"));
                list.add(t);
            }
        }
        return list;
    }
}
