package com.example.crm.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AnalyticsPanel extends JPanel {
    
    public AnalyticsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Analytics & Reporting Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JButton exportBtn = createStyledButton("Export Report", new Color(52, 152, 219));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(exportBtn, BorderLayout.EAST);
        
        // Main content panel with grid layout
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // KPI Cards
        contentPanel.add(createKPICard("Total Customers", "1,234", "+12%", new Color(52, 152, 219)));
        contentPanel.add(createKPICard("Total Leads", "567", "+8%", new Color(46, 204, 113)));
        contentPanel.add(createKPICard("Total Sales", "$245,890", "+25%", new Color(241, 196, 15)));
        contentPanel.add(createKPICard("Conversion Rate", "23.5%", "+3.2%", new Color(230, 126, 34)));
        
        // Bottom section with charts placeholder
        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        chartPanel.setBackground(new Color(236, 240, 241));
        chartPanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        
        chartPanel.add(createChartCard("Sales Trend", "Monthly sales performance chart"));
        chartPanel.add(createChartCard("Lead Sources", "Lead distribution by source"));
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.add(contentPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createKPICard(String title, String value, String change, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel changeLabel = new JLabel(change + " from last month");
        changeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changeLabel.setForeground(new Color(46, 204, 113));
        changeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(changeLabel);
        
        return card;
    }
    
    private JPanel createChartCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        
        JPanel chartArea = new JPanel();
        chartArea.setBackground(new Color(236, 240, 241));
        chartArea.setPreferredSize(new Dimension(400, 250));
        
        JLabel placeholderLabel = new JLabel(description, JLabel.CENTER);
        placeholderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholderLabel.setForeground(new Color(149, 165, 166));
        chartArea.setLayout(new BorderLayout());
        chartArea.add(placeholderLabel, BorderLayout.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(chartArea, BorderLayout.CENTER);
        
        return card;
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
