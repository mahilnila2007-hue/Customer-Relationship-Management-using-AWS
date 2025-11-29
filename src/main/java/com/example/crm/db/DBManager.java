package com.example.crm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    private static final String DB_URL = "jdbc:sqlite:crm.db";

    static {
        try {
            initDatabase();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void initDatabase() throws SQLException {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            // Customers
            s.execute("CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT, phone TEXT, company TEXT, notes TEXT)");
            // Leads
            s.execute("CREATE TABLE IF NOT EXISTS leads (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT, phone TEXT, source TEXT, status TEXT, notes TEXT)");
            // Sales
            s.execute("CREATE TABLE IF NOT EXISTS sales (id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, amount REAL, description TEXT, date TEXT, FOREIGN KEY(customer_id) REFERENCES customers(id))");
            // Campaigns
            s.execute("CREATE TABLE IF NOT EXISTS campaigns (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, channel TEXT, start_date TEXT, end_date TEXT, budget REAL, notes TEXT)");
            // Tickets
            s.execute("CREATE TABLE IF NOT EXISTS tickets (id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, subject TEXT, description TEXT, status TEXT, priority TEXT, created_at TEXT, updated_at TEXT, FOREIGN KEY(customer_id) REFERENCES customers(id))");
            // Users - Enhanced with approval workflow
            s.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, role TEXT NOT NULL, status TEXT DEFAULT 'PENDING', full_name TEXT, email TEXT, registration_date TEXT, last_login TEXT)");
            // Interactions (history)
            s.execute("CREATE TABLE IF NOT EXISTS interactions (id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER, type TEXT, note TEXT, date TEXT, FOREIGN KEY(customer_id) REFERENCES customers(id))");
            
            // Create default admin if not exists
            s.execute("INSERT OR IGNORE INTO users(id, username, password, role, status, full_name, registration_date) VALUES(1, 'kaviya', 'kaviya123', 'ADMIN', 'APPROVED', 'Kaviya Administrator', datetime('now'))");
        }
    }
}
