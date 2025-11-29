package com.example.crm;

import javax.swing.SwingUtilities;
import com.example.crm.ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting CRM application...");
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
