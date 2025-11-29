package com.example.crm.service;

import com.example.crm.dao.UserDAO;
import com.example.crm.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO dao = new UserDAO();

    public User createUser(User u) throws ServiceException {
        try {
            if (u.getUsername() == null || u.getUsername().isBlank()) throw new IllegalArgumentException("username required");
            if (u.getPassword() == null || u.getPassword().isBlank()) throw new IllegalArgumentException("password required");
            if (u.getRole() == null) u.setRole("SALES");
            return dao.create(u);
        } catch (SQLException | IllegalArgumentException e) {
            throw new ServiceException("Failed to create user: " + e.getMessage(), e);
        }
    }

    public User authenticate(String username, String password) throws ServiceException {
        try {
            if (username == null || password == null) return null;
            return dao.authenticate(username, password);
        } catch (SQLException e) { 
            throw new ServiceException("Authentication failed", e); 
        }
    }
    
    public List<User> listUsers() throws ServiceException {
        try {
            return dao.findAll();
        } catch (SQLException e) {
            throw new ServiceException("Failed to list users: " + e.getMessage(), e);
        }
    }
    
    public List<User> listPendingUsers() throws ServiceException {
        try {
            return dao.findPending();
        } catch (SQLException e) {
            throw new ServiceException("Failed to list pending users: " + e.getMessage(), e);
        }
    }
    
    public void approveUser(int userId) throws ServiceException {
        try {
            dao.updateStatus(userId, "APPROVED");
        } catch (SQLException e) {
            throw new ServiceException("Failed to approve user: " + e.getMessage(), e);
        }
    }
    
    public void rejectUser(int userId) throws ServiceException {
        try {
            dao.updateStatus(userId, "REJECTED");
        } catch (SQLException e) {
            throw new ServiceException("Failed to reject user: " + e.getMessage(), e);
        }
    }
    
    public void deleteUser(int userId) throws ServiceException {
        try {
            dao.delete(userId);
        } catch (SQLException e) {
            throw new ServiceException("Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    public void updateUser(User u) throws ServiceException {
        try {
            dao.updateUser(u);
        } catch (SQLException e) {
            throw new ServiceException("Failed to update user: " + e.getMessage(), e);
        }
    }
}
