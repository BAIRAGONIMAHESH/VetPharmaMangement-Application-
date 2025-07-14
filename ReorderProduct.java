package loginapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReorderProduct extends JFrame {

    private float opacity = 0.0f;
    private JTable reorderTable;
    private DefaultTableModel tableModel;

    public ReorderProduct() {
        setTitle("Reorder Products");
        setSize(900, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setOpacity(opacity);

        // 🔹 Animated background
        AnimatedBackground bg = new AnimatedBackground();
        bg.setBounds(0, 0, 900, 500);
        setContentPane(bg);
        bg.setLayout(null);

        // 🔹 Title bar
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
        maxBtn.addActionListener(e -> {
            int state = getExtendedState();
            setExtendedState((state == JFrame.MAXIMIZED_BOTH) ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
        });
        topBar.add(maxBtn);

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(860, 5, 30, 30);
        closeBtn.setForeground(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        topBar.add(closeBtn);

        // 🔹 Page title
        JLabel title = new JLabel("Reorder Products", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 45, 900, 30);
        bg.add(title);

        // 🔹 Table Panel
        JPanel tablePanel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 70));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        tablePanel.setBounds(50, 90, 800, 370);
        tablePanel.setOpaque(false);
        bg.add(tablePanel);

        // 🔹 Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Quantity", "Reorder"}, 0);
        reorderTable = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        reorderTable.getColumn("Reorder").setCellRenderer(new ButtonRenderer());
        reorderTable.getColumn("Reorder").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(reorderTable);
        scrollPane.setBounds(20, 20, 760, 250);
        tablePanel.add(scrollPane);

        // 🔹 Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(680, 290, 100, 30);
        styleButton(refreshBtn);
        tablePanel.add(refreshBtn);
        refreshBtn.addActionListener(e -> loadLowStock());

        loadLowStock();
        fadeIn();
        setVisible(true);
    }

    // 🔹 Load data from database
    private void loadLowStock() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "Mahesh@06122001");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE quantity <= 10");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        "Reorder"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching reorder data: " + e.getMessage());
        }
    }

    // 🔹 Button to reorder
    private void reorderProduct(int id) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reorder Product ID " + id + "?", "Confirm Reorder",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Reorder placed successfully for Product ID: " + id);
        }
    }

    // 🔹 Top Title Buttons
    private JButton createTitleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(true);
        return btn;
    }

    // 🔹 Style normal buttons
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(39, 174, 96));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(true);
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

    // 🔹 Button in JTable
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Reorder");
            setForeground(Color.WHITE);
            setBackground(new Color(39, 174, 96));
            setFocusPainted(false);
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder());
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Reorder");
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(39, 174, 96));
            button.setFocusPainted(false);
            button.setFocusable(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(true);

            button.addActionListener(e -> {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                reorderProduct(id);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }
    }

    // 🔹 Background animation
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
            Color c1 = Color.getHSBColor(hue, 0.4f, 1.0f);
            Color c2 = Color.getHSBColor(hue + 0.1f, 0.4f, 1.0f);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new ReorderProduct();
    }
}
