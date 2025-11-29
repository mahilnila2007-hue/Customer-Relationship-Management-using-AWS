package com.example.crm.ui;

import com.example.crm.model.Sale;
import com.example.crm.service.SaleService;
import com.example.crm.service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SalesPanel extends JPanel {
    private final SaleService service = new SaleService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Customer ID","Amount","Description","Date"},0){@Override public boolean isCellEditable(int r,int c){return false;}};

    public SalesPanel(){
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(0,2,6,6));
        JTextField customerId = new JTextField(); JTextField amount = new JTextField(); JTextField desc = new JTextField(); JTextField date = new JTextField();
        top.add(new JLabel("Customer ID:")); top.add(customerId);
        top.add(new JLabel("Amount:")); top.add(amount);
        top.add(new JLabel("Description:")); top.add(desc);
        top.add(new JLabel("Date (YYYY-MM-DD):")); top.add(date);
        JButton create = new JButton("Create Sale"); JButton refresh = new JButton("Refresh");
        JPanel ctrl = new JPanel(); ctrl.add(create); ctrl.add(refresh);
        JTable table = new JTable(model);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(ctrl, BorderLayout.SOUTH);

        create.addActionListener(e->{
            try {
                Sale s = new Sale(); s.setCustomerId(Integer.valueOf(customerId.getText())); s.setAmount(Double.parseDouble(amount.getText())); s.setDescription(desc.getText()); s.setDate(date.getText());
                service.createSale(s); refreshList(); customerId.setText(""); amount.setText(""); desc.setText(""); date.setText("");
            } catch (Exception ex){ JOptionPane.showMessageDialog(this, "Create sale failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        refresh.addActionListener(e->refreshList());
        refreshList();
    }

    private void refreshList(){
        try { java.util.List<Sale> list = service.listSales(); model.setRowCount(0); for(Sale s:list) model.addRow(new Object[]{s.getId(), s.getCustomerId(), s.getAmount(), s.getDescription(), s.getDate()}); }
        catch (ServiceException e){ JOptionPane.showMessageDialog(this, "Load failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }
}
