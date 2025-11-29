package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.Lead;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeadDAO {
    public Lead create(Lead l) throws SQLException {
        String sql = "INSERT INTO leads(name,email,phone,source,status,notes) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getName()); ps.setString(2, l.getEmail()); ps.setString(3, l.getPhone()); ps.setString(4, l.getSource()); ps.setString(5, l.getStatus()); ps.setString(6, l.getNotes());
            ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) l.setId(rs.getInt(1)); }
        }
        return l;
    }

    public List<Lead> findAll() throws SQLException {
        List<Lead> list = new ArrayList<>();
        String sql = "SELECT id,name,email,phone,source,status,notes FROM leads";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Lead l = new Lead();
                l.setId(rs.getInt("id")); l.setName(rs.getString("name")); l.setEmail(rs.getString("email")); l.setPhone(rs.getString("phone"));
                l.setSource(rs.getString("source")); l.setStatus(rs.getString("status")); l.setNotes(rs.getString("notes"));
                list.add(l);
            }
        }
        return list;
    }
}
