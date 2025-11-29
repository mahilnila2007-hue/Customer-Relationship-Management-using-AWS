package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    public Sale create(Sale s) throws SQLException {
        String sql = "INSERT INTO sales(customer_id,amount,description,date) VALUES(?,?,?,?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, s.getCustomerId()); ps.setDouble(2, s.getAmount()); ps.setString(3, s.getDescription()); ps.setString(4, s.getDate());
            ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) s.setId(rs.getInt(1)); }
        }
        return s;
    }

    public List<Sale> findAll() throws SQLException {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT id,customer_id,amount,description,date FROM sales";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Sale s = new Sale(); s.setId(rs.getInt("id")); s.setCustomerId(rs.getInt("customer_id")); s.setAmount(rs.getDouble("amount")); s.setDescription(rs.getString("description")); s.setDate(rs.getString("date"));
                list.add(s);
            }
        }
        return list;
    }
}
