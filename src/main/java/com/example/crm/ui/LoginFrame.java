package com.example.crm.ui;

import com.example.crm.dao.UserDAO;
import com.example.crm.model.User;
import com.example.crm.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO = new UserDAO();
    
    public LoginFrame() {
        super("CRM System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(true);
        initUI();
    }
    
    private void initUI() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), 0, h, new Color(109, 213, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Login card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        cardPanel.setPreferredSize(new Dimension(380, 450));
        
        // Logo/Title
        JLabel titleLabel = new JLabel("CRM System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Customer Relationship Management");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        cardPanel.add(userLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        cardPanel.add(passLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Login button
        JButton loginBtn = createStyledButton("Login", new Color(52, 152, 219));
        loginBtn.addActionListener(e -> handleLogin());
        cardPanel.add(loginBtn);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Register button
        JButton registerBtn = createStyledButton("Register New Account", new Color(46, 204, 113));
        registerBtn.addActionListener(e -> showRegistrationDialog());
        cardPanel.add(registerBtn);
        
        // Add card to main panel
        mainPanel.add(cardPanel);
        
        // Enter key listener
        KeyAdapter enterListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
        
        setContentPane(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        try {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                });
            } else {
                // Check if user exists but not approved
                User pendingUser = userDAO.findByUsername(username);
                if (pendingUser != null && "PENDING".equals(pendingUser.getStatus())) {
                    showError("Your account is pending approval by an administrator.");
                } else if (pendingUser != null && "REJECTED".equals(pendingUser.getStatus())) {
                    showError("Your account has been rejected.");
                } else {
                    showError("Invalid username or password");
                }
            }
        } catch (Exception ex) {
            showError("Login error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void showRegistrationDialog() {
        JDialog regDialog = new JDialog(this, "Register New Account", true);
        regDialog.setSize(400, 450);
        regDialog.setLocationRelativeTo(this);
        regDialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField regUsername = new JTextField(20);
        JPasswordField regPassword = new JPasswordField(20);
        JPasswordField regConfirmPass = new JPasswordField(20);
        JTextField regFullName = new JTextField(20);
        JTextField regEmail = new JTextField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"SALES", "SUPPORT", "MANAGER"});
        
        addFormField(formPanel, gbc, 0, "Username:", regUsername);
        addFormField(formPanel, gbc, 1, "Password:", regPassword);
        addFormField(formPanel, gbc, 2, "Confirm Password:", regConfirmPass);
        addFormField(formPanel, gbc, 3, "Full Name:", regFullName);
        addFormField(formPanel, gbc, 4, "Email:", regEmail);
        addFormField(formPanel, gbc, 5, "Role:", roleCombo);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit Registration");
        JButton cancelBtn = new JButton("Cancel");
        
        submitBtn.addActionListener(e -> {
            String pass = new String(regPassword.getPassword());
            String confirmPass = new String(regConfirmPass.getPassword());
            
            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(regDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (regUsername.getText().trim().isEmpty() || pass.isEmpty() || regFullName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(regDialog, "Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                User newUser = new User();
                newUser.setUsername(regUsername.getText().trim());
                newUser.setPassword(pass);
                newUser.setFullName(regFullName.getText().trim());
                newUser.setEmail(regEmail.getText().trim());
                newUser.setRole((String) roleCombo.getSelectedItem());
                newUser.setStatus("PENDING");
                
                userDAO.create(newUser);
                JOptionPane.showMessageDialog(regDialog, 
                    "Registration successful!\n\nYour account is pending approval by an administrator.\nYou will be able to login once approved.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                regDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(regDialog, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> regDialog.dispose());
        
        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn);
        
        regDialog.add(formPanel, BorderLayout.CENTER);
        regDialog.add(btnPanel, BorderLayout.SOUTH);
        regDialog.setVisible(true);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
