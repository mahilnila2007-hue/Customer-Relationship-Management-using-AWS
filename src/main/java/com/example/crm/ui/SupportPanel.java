package com.example.crm.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SupportPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    
    public SupportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 126, 34));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Support Tickets");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JButton newTicketBtn = createStyledButton("+ New Ticket", new Color(46, 204, 113));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(newTicketBtn, BorderLayout.EAST);
        
        // Table
        String[] columns = {"Ticket ID", "Customer", "Subject", "Priority", "Status", "Assigned To", "Created Date", "Last Update"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Sample data
        tableModel.addRow(new Object[]{"TKT-1001", "John Smith", "Login Issue", "High", "In Progress", "Sarah Lee", "2025-10-20", "2 hours ago"});
        tableModel.addRow(new Object[]{"TKT-1002", "Mary Johnson", "Feature Request", "Medium", "Open", "Unassigned", "2025-10-21", "30 mins ago"});
        tableModel.addRow(new Object[]{"TKT-1003", "Bob Williams", "Bug Report", "Critical", "In Progress", "Mike Chen", "2025-10-21", "1 hour ago"});
        tableModel.addRow(new Object[]{"TKT-1004", "Alice Brown", "Billing Question", "Low", "Resolved", "Emma Davis", "2025-10-19", "1 day ago"});
        tableModel.addRow(new Object[]{"TKT-1005", "Tom Wilson", "Account Access", "High", "Open", "Unassigned", "2025-10-21", "15 mins ago"});
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(236, 240, 241));
        table.setSelectionBackground(new Color(255, 179, 102));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(createStyledButton("View Ticket", new Color(52, 152, 219)));
        buttonPanel.add(createStyledButton("Assign", new Color(241, 196, 15)));
        buttonPanel.add(createStyledButton("Update Status", new Color(155, 89, 182)));
        buttonPanel.add(createStyledButton("Close Ticket", new Color(46, 204, 113)));
        buttonPanel.add(createStyledButton("Delete", new Color(231, 76, 60)));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
        return btn;
    }
}
