package com.example.crm.dao;

import com.example.crm.db.DBManager;
import com.example.crm.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User create(User u) throws SQLException {
        if (u.getUsername() == null || u.getUsername().isBlank()) throw new IllegalArgumentException("username required");
        if (u.getPassword() == null || u.getPassword().isBlank()) throw new IllegalArgumentException("password required");
        
        String sql = "INSERT INTO users(username,password,role,status,full_name,email,registration_date) VALUES(?,?,?,?,?,?,datetime('now'))";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole() != null ? u.getRole() : "SALES");
            ps.setString(4, u.getStatus() != null ? u.getStatus() : "PENDING");
            ps.setString(5, u.getFullName());
            ps.setString(6, u.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { 
                if (rs.next()) u.setId(rs.getInt(1)); 
            }
        }
        return u;
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }
    
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }
    
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection conn = DBManager.getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }
        return users;
    }
    
    public List<User> findPending() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status='PENDING' ORDER BY registration_date";
        try (Connection conn = DBManager.getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }
        return users;
    }
    
    public void updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status=? WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
    
    public void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login=datetime('now') WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
    
    public void updateUser(User u) throws SQLException {
        String sql = "UPDATE users SET full_name=?, email=?, role=? WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRole());
            ps.setInt(4, u.getId());
            ps.executeUpdate();
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public User authenticate(String username, String password) throws SQLException {
        User u = findByUsername(username);
        if (u == null) return null;
        if (!"APPROVED".equals(u.getStatus())) return null; // Only approved users can login
        // In production compare hashed passwords
        if (!password.equals(u.getPassword())) return null;
        
        updateLastLogin(u.getId());
        return u;
    }
    
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setStatus(rs.getString("status"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setRegistrationDate(rs.getString("registration_date"));
        u.setLastLogin(rs.getString("last_login"));
        return u;
    }
}
