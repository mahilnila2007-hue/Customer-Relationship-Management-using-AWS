package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public Customer create(Customer c) throws SQLException {
        if (c.getName() == null || c.getName().isBlank()) throw new IllegalArgumentException("Customer name is required");
        String sql = "INSERT INTO customers(name,email,phone,company,notes) VALUES(?,?,?,?,?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getCompany());
            ps.setString(5, c.getNotes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
        return c;
    }

    public Customer findById(int id) throws SQLException {
        String sql = "SELECT id,name,email,phone,company,notes FROM customers WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT id,name,email,phone,company,notes FROM customers";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean update(Customer c) throws SQLException {
        if (c.getId() == null) throw new IllegalArgumentException("Customer ID required for update");
        String sql = "UPDATE customers SET name=?,email=?,phone=?,company=?,notes=? WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getCompany());
            ps.setString(5, c.getNotes());
            ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setCompany(rs.getString("company"));
        c.setNotes(rs.getString("notes"));
        return c;
    }
}
