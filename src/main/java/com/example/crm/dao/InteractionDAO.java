package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.Interaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteractionDAO {
    public Interaction create(Interaction i) throws SQLException {
        String sql = "INSERT INTO interactions(customer_id,type,note,date) VALUES(?,?,?,?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, i.getCustomerId()); ps.setString(2, i.getType()); ps.setString(3, i.getNote()); ps.setString(4, i.getDate());
            ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) i.setId(rs.getInt(1)); }
        }
        return i;
    }

    public List<Interaction> findAllByCustomer(int customerId) throws SQLException {
        List<Interaction> list = new ArrayList<>();
        String sql = "SELECT id,customer_id,type,note,date FROM interactions WHERE customer_id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Interaction i = new Interaction(); i.setId(rs.getInt("id")); i.setCustomerId(rs.getInt("customer_id")); i.setType(rs.getString("type")); i.setNote(rs.getString("note")); i.setDate(rs.getString("date"));
                    list.add(i);
                }
            }
        }
        return list;
    }
}
