package com.example.crm.ui;

import com.example.crm.model.Lead;
import com.example.crm.service.LeadService;
import com.example.crm.service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeadPanel extends JPanel {
    private final LeadService service = new LeadService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Name","Email","Phone","Source","Status"},0){@Override public boolean isCellEditable(int r,int c){return false;}};

    public LeadPanel(){
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(0,2,6,6));
        JTextField name = new JTextField(); JTextField email = new JTextField(); JTextField phone = new JTextField(); JTextField source = new JTextField();
        top.add(new JLabel("Name:")); top.add(name);
        top.add(new JLabel("Email:")); top.add(email);
        top.add(new JLabel("Phone:")); top.add(phone);
        top.add(new JLabel("Source:")); top.add(source);
        JButton create = new JButton("Create Lead"); JButton refresh = new JButton("Refresh");
        JPanel ctrl = new JPanel(); ctrl.add(create); ctrl.add(refresh);
        JTable table = new JTable(model);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(ctrl, BorderLayout.SOUTH);

        create.addActionListener(e->{
            Lead l = new Lead(); l.setName(name.getText()); l.setEmail(email.getText()); l.setPhone(phone.getText()); l.setSource(source.getText()); l.setStatus("New");
            try { service.createLead(l); refreshList(); name.setText(""); email.setText(""); phone.setText(""); source.setText(""); }
            catch (ServiceException ex){ JOptionPane.showMessageDialog(this, "Lead create failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        refresh.addActionListener(e->refreshList());
        refreshList();
    }

    private void refreshList(){
        try { java.util.List<Lead> list = service.listLeads(); model.setRowCount(0); for(Lead l:list) model.addRow(new Object[]{l.getId(), l.getName(), l.getEmail(), l.getPhone(), l.getSource(), l.getStatus()}); }
        catch (ServiceException e){ JOptionPane.showMessageDialog(this, "Load failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }
}
