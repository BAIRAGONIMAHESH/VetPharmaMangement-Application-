package loginapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ExpiryAlert extends JFrame {

    private float opacity = 0.0f;
    private JTable expiryTable;
    private DefaultTableModel tableModel;

    public ExpiryAlert() {
        setTitle("Expiry Alert");
        setSize(900, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setOpacity(opacity);

        // Top bar with Home, Min, Max, Close
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 900, 40);
        titleBar.setBackground(Color.DARK_GRAY);
        add(titleBar);

        JButton homeBtn = createTitleButton("Home");
        homeBtn.setBounds(5, 5, 60, 30);
        titleBar.add(homeBtn);
        homeBtn.addActionListener(e -> {
            new RoleAccess("admin", "admin");
            dispose();
        });

        JButton minBtn = createTitleButton("—");
        minBtn.setBounds(800, 5, 30, 30);
        titleBar.add(minBtn);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton maxBtn = createTitleButton("☐");
        maxBtn.setBounds(830, 5, 30, 30);
        titleBar.add(maxBtn);
        maxBtn.addActionListener(e -> setExtendedState(getExtendedState() == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH));

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(860, 5, 30, 30);
        closeBtn.setForeground(Color.RED);
        titleBar.add(closeBtn);
        closeBtn.addActionListener(e -> System.exit(0));

        // Animated Background
        AnimatedBackground bg = new AnimatedBackground();
        bg.setBounds(0, 0, 900, 500);
        bg.setLayout(null);
        add(bg);

        // Title
        JLabel title = new JLabel("Products Nearing Expiry", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 50, 900, 30);
        bg.add(title);

        // Table Panel
        JPanel tablePanel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        tablePanel.setBounds(50, 100, 800, 320);
        tablePanel.setOpaque(false);
        bg.add(tablePanel);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Expiry Date", "Status"}, 0);
        expiryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expiryTable);
        scrollPane.setBounds(20, 20, 760, 250);
        tablePanel.add(scrollPane);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(680, 280, 100, 30);
        styleActionButton(refreshBtn);
        tablePanel.add(refreshBtn);
        refreshBtn.addActionListener(e -> loadExpiringSoon());

        loadExpiringSoon();
        fadeIn();
        setVisible(true);
    }

    private void loadExpiringSoon() {
        tableModel.setRowCount(0);

        String query = "SELECT id, name, category, expiry_date FROM products " +
                       "WHERE expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "Mahesh@06122001");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDate("expiry_date"),
                        "Expiring Soon"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching expiry data: " + e.getMessage());
        }
    }

    private JButton createTitleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleActionButton(JButton btn) {
        btn.setBackground(new Color(231, 76, 60));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(true);
    }

    private void fadeIn() {
        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                opacity = 1.0f;
                setOpacity(opacity);
                ((Timer) e.getSource()).stop();
            } else {
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    class AnimatedBackground extends JPanel {
        private float hue = 0.0f;

        public AnimatedBackground() {
            Timer timer = new Timer(50, e -> {
                hue += 0.003f;
                if (hue >= 1.0f) hue = 0.0f;
                repaint();
            });
            timer.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color c1 = Color.getHSBColor(hue, 0.6f, 1.0f);
            Color c2 = Color.getHSBColor(hue + 0.1f, 0.6f, 1.0f);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new ExpiryAlert();
    }
}
