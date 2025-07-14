package loginapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ExportData extends JFrame {

    private float opacity = 0.0f;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public ExportData() {
        setTitle("Export Product Data");
        setSize(900, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setOpacity(opacity);

        // 🔹 Animated Background
        AnimatedBackground bg = new AnimatedBackground();
        bg.setBounds(0, 0, 900, 500);
        setContentPane(bg);
        bg.setLayout(null);

        // 🔹 Top Bar
        JPanel topBar = new JPanel(null);
        topBar.setBounds(0, 0, 900, 40);
        topBar.setBackground(Color.DARK_GRAY);
        bg.add(topBar);

        JButton homeBtn = createTitleButton("Home");
        homeBtn.setBounds(5, 5, 60, 30);
        homeBtn.addActionListener(e -> {
            new RoleAccess("admin", "admin");
            dispose();
        });
        topBar.add(homeBtn);

        JButton minBtn = createTitleButton("—");
        minBtn.setBounds(800, 5, 30, 30);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        topBar.add(minBtn);

        JButton maxBtn = createTitleButton("☐");
        maxBtn.setBounds(830, 5, 30, 30);
        maxBtn.addActionListener(e -> setExtendedState(getExtendedState() == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH));
        topBar.add(maxBtn);

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(860, 5, 30, 30);
        closeBtn.setForeground(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        topBar.add(closeBtn);

        // 🔹 Title Label
        JLabel title = new JLabel("Export Product List", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 45, 900, 30);
        bg.add(title);

        // 🔹 Rounded Panel
        JPanel panel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        panel.setBounds(50, 80, 800, 370);
        panel.setOpaque(false);
        bg.add(panel);

        // 🔹 Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Quantity"}, 0);
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(20, 20, 760, 250);
        panel.add(scrollPane);

        // 🔹 Buttons
        JButton exportPDF = new JButton("Export to PDF");
        JButton exportExcel = new JButton("Export to Excel");
        styleButton(exportPDF, new Color(52, 73, 94));
        styleButton(exportExcel, new Color(22, 160, 133));
        exportPDF.setBounds(150, 290, 200, 40);
        exportExcel.setBounds(430, 290, 200, 40);
        panel.add(exportPDF);
        panel.add(exportExcel);

        exportPDF.addActionListener(e -> JOptionPane.showMessageDialog(this, "PDF Export coming soon!"));
        exportExcel.addActionListener(e -> JOptionPane.showMessageDialog(this, "Excel Export coming soon!"));

        loadData();
        fadeIn();
        setVisible(true);
    }

    // 🔹 Load data from MySQL
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "Mahesh@06122001");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // 🔹 Title Buttons: No focus/hover dot
    private JButton createTitleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusable(false); // 💥 This removes dotted outline
        return btn;
    }

    // 🔹 Styling Export Buttons
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }

    // 🔹 Fade in animation
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

    // 🔹 Animated Background
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
            Color c1 = Color.getHSBColor(hue, 0.5f, 1.0f);
            Color c2 = Color.getHSBColor(hue + 0.1f, 0.5f, 1.0f);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new ExportData();
    }
}
