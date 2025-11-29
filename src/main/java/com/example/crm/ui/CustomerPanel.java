package com.example.crm.ui;

import com.example.crm.model.Customer;
import com.example.crm.service.CustomerService;
import com.example.crm.service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CustomerPanel extends JPanel {
    private final CustomerService service = new CustomerService();
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone", "Company"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public CustomerPanel() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField companyField = new JTextField();

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("Company:")); form.add(companyField);

        JButton createBtn = new JButton("Create");
        JButton refreshBtn = new JButton("Refresh");
        JButton deleteBtn = new JButton("Delete Selected");

        JPanel controls = new JPanel();
        controls.add(createBtn); controls.add(refreshBtn); controls.add(deleteBtn);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        createBtn.addActionListener((ActionEvent e) -> {
            Customer c = new Customer();
            c.setName(nameField.getText());
            c.setEmail(emailField.getText());
            c.setPhone(phoneField.getText());
            c.setCompany(companyField.getText());
            try {
                service.createCustomer(c);
                refreshList();
                nameField.setText(""); emailField.setText(""); phoneField.setText(""); companyField.setText("");
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> refreshList());

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            int id = (int) tableModel.getValueAt(row, 0);
            try {
                service.deleteCustomer(id);
                refreshList();
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshList();
    }

    private void refreshList() {
        try {
            List<Customer> list = service.listCustomers();
            tableModel.setRowCount(0);
            for (Customer c : list) tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getCompany()});
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
