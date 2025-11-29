package com.example.crm.ui;

import com.example.crm.dao.UserDAO;
import com.example.crm.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private UserDAO userDAO = new UserDAO();
    private JTable pendingTable;
    private JTable allUsersTable;
    private DefaultTableModel pendingModel;
    private DefaultTableModel allUsersModel;
    
    public UserManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        initUI();
        loadData();
    }
    
    private void initUI() {
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setForeground(new Color(52, 152, 219));
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadData());
        titlePanel.add(refreshBtn, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Tabbed pane for pending and all users
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Pending approvals tab
        tabbedPane.addTab("Pending Approvals", createPendingPanel());
        
        // All users tab
        tabbedPane.addTab("All Users", createAllUsersPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createPendingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Table for pending users
        String[] columns = {"ID", "Username", "Full Name", "Email", "Role", "Registration Date"};
        pendingModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        pendingTable = new JTable(pendingModel);
        pendingTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pendingTable.setRowHeight(25);
        pendingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingTable.getTableHeader().setBackground(new Color(52, 152, 219));
        pendingTable.getTableHeader().setForeground(Color.WHITE);
        pendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(pendingTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton approveBtn = new JButton("Approve");
        approveBtn.setBackground(new Color(46, 204, 113));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFocusPainted(false);
        approveBtn.addActionListener(e -> handleApprove());
        
        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(231, 76, 60));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFocusPainted(false);
        rejectBtn.addActionListener(e -> handleReject());
        
        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAllUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Table for all users
        String[] columns = {"ID", "Username", "Full Name", "Email", "Role", "Status", "Last Login"};
        allUsersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        allUsersTable = new JTable(allUsersModel);
        allUsersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        allUsersTable.setRowHeight(25);
        allUsersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        allUsersTable.getTableHeader().setBackground(new Color(52, 152, 219));
        allUsersTable.getTableHeader().setForeground(Color.WHITE);
        allUsersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(allUsersTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton editBtn = new JButton("Edit Role");
        editBtn.setBackground(new Color(52, 152, 219));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> handleEditRole());
        
        JButton deleteBtn = new JButton("Delete User");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> handleDeleteUser());
        
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadData() {
        try {
            // Load pending users
            pendingModel.setRowCount(0);
            List<User> pendingUsers = userDAO.findPending();
            for (User user : pendingUsers) {
                pendingModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getRegistrationDate()
                });
            }
            
            // Load all users
            allUsersModel.setRowCount(0);
            List<User> allUsers = userDAO.findAll();
            for (User user : allUsers) {
                allUsersModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getStatus(),
                    user.getLastLogin() != null ? user.getLastLogin() : "Never"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void handleApprove() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to approve", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int userId = (Integer) pendingModel.getValueAt(selectedRow, 0);
            String username = (String) pendingModel.getValueAt(selectedRow, 1);
            
            int result = JOptionPane.showConfirmDialog(this, 
                "Approve user '" + username + "'?", 
                "Confirm Approval", 
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                userDAO.updateStatus(userId, "APPROVED");
                JOptionPane.showMessageDialog(this, "User approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error approving user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleReject() {
        int selectedRow = pendingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to reject", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int userId = (Integer) pendingModel.getValueAt(selectedRow, 0);
            String username = (String) pendingModel.getValueAt(selectedRow, 1);
            
            int result = JOptionPane.showConfirmDialog(this, 
                "Reject user '" + username + "'?", 
                "Confirm Rejection", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                userDAO.updateStatus(userId, "REJECTED");
                JOptionPane.showMessageDialog(this, "User rejected", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error rejecting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleEditRole() {
        int selectedRow = allUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int userId = (Integer) allUsersModel.getValueAt(selectedRow, 0);
            String currentRole = (String) allUsersModel.getValueAt(selectedRow, 4);
            
            String[] roles = {"ADMIN", "MANAGER", "SALES", "SUPPORT"};
            String newRole = (String) JOptionPane.showInputDialog(this, 
                "Select new role:", 
                "Edit User Role", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                roles, 
                currentRole);
            
            if (newRole != null && !newRole.equals(currentRole)) {
                User user = userDAO.findById(userId);
                user.setRole(newRole);
                userDAO.updateUser(user);
                JOptionPane.showMessageDialog(this, "User role updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating role: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleDeleteUser() {
        int selectedRow = allUsersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int userId = (Integer) allUsersModel.getValueAt(selectedRow, 0);
            String username = (String) allUsersModel.getValueAt(selectedRow, 1);
            
            if (userId == 1) {
                JOptionPane.showMessageDialog(this, "Cannot delete the system administrator!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete user '" + username + "'?\n\nThis action cannot be undone!", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                userDAO.delete(userId);
                JOptionPane.showMessageDialog(this, "User deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
