package com.example.crm.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MarketingPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    
    public MarketingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Marketing Campaigns");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JButton newCampaignBtn = createStyledButton("+ New Campaign", new Color(46, 204, 113));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(newCampaignBtn, BorderLayout.EAST);
        
        // Table
        String[] columns = {"ID", "Campaign Name", "Type", "Start Date", "End Date", "Budget", "Status", "ROI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Sample data
        tableModel.addRow(new Object[]{1, "Summer Sale 2025", "Email", "2025-06-01", "2025-06-30", "$5,000", "Active", "145%"});
        tableModel.addRow(new Object[]{2, "Product Launch", "Social Media", "2025-07-15", "2025-08-15", "$10,000", "Planning", "N/A"});
        tableModel.addRow(new Object[]{3, "Black Friday", "Multi-channel", "2025-11-20", "2025-11-30", "$25,000", "Planning", "N/A"});
        tableModel.addRow(new Object[]{4, "Customer Retention", "Email", "2025-05-01", "2025-12-31", "$8,000", "Active", "123%"});
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(236, 240, 241));
        table.setSelectionBackground(new Color(174, 214, 241));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(createStyledButton("View Details", new Color(52, 152, 219)));
        buttonPanel.add(createStyledButton("Edit Campaign", new Color(241, 196, 15)));
        buttonPanel.add(createStyledButton("View Analytics", new Color(155, 89, 182)));
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
