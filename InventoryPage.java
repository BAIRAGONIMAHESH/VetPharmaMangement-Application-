package loginapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class InventoryPage extends JFrame {

    private float opacity = 0.0f;
    private JTable table;
    private DefaultTableModel model;

    public InventoryPage() {
        setTitle("Inventory Overview");
        setSize(900, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setOpacity(opacity);

        // 🔹 Animated Background
        AnimatedBackground bg = new AnimatedBackground();
        setContentPane(bg);
        bg.setLayout(null);

        // 🔹 Top Bar
        JPanel topBar = new JPanel(null);
        topBar.setBounds(0, 0, 900, 40);
        topBar.setBackground(Color.DARK_GRAY);
        bg.add(topBar);

        JButton homeBtn = createTitleButton("Home");
        homeBtn.setBounds(5, 5, 60, 30);
        topBar.add(homeBtn);
        homeBtn.addActionListener(e -> {
            new RoleAccess("admin", "admin");
            dispose();
        });

        JButton minBtn = createTitleButton("—");
        minBtn.setBounds(800, 5, 30, 30);
        topBar.add(minBtn);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton maxBtn = createTitleButton("☐");
        maxBtn.setBounds(830, 5, 30, 30);
        topBar.add(maxBtn);
        maxBtn.addActionListener(e -> {
            int state = getExtendedState();
            setExtendedState((state == JFrame.MAXIMIZED_BOTH) ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
        });

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(860, 5, 30, 30);
        closeBtn.setForeground(Color.RED);
        topBar.add(closeBtn);
        closeBtn.addActionListener(e -> System.exit(0));

        // 🔹 Title Label
        JLabel title = new JLabel("Inventory Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 45, 900, 40);
        bg.add(title);

        // 🔹 Table Panel
        JPanel tablePanel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        tablePanel.setBounds(50, 100, 800, 350);
        tablePanel.setOpaque(false);
        bg.add(tablePanel);

        // 🔹 Table
        model = new DefaultTableModel(new String[]{"Name", "Category", "Price", "Quantity"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setBackground(new Color(255, 255, 255, 230));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 760, 300);
        tablePanel.add(scrollPane);

        // 🔹 Dummy Inventory Data
        addInventoryData();

        // 🔹 Show GUI
        fadeIn();
        setVisible(true);
    }

    private void addInventoryData() {
        // Sample inventory data
        ArrayList<String[]> inventory = new ArrayList<>();
        inventory.add(new String[]{"Amoxicillin", "Antibiotic", "25", "50"});
        inventory.add(new String[]{"Vitamin D", "Supplement", "40", "120"});
        inventory.add(new String[]{"Ivermectin", "Dewormer", "30", "80"});
        inventory.add(new String[]{"Ceftriaxone", "Antibiotic", "45", "60"});
        inventory.add(new String[]{"Iron Syrup", "Supplement", "22", "90"});

        for (String[] item : inventory) {
            model.addRow(item);
        }
    }

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
        btn.setFocusable(false);
        return btn;
    }

    private void fadeIn() {
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                setOpacity(1.0f);
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
                hue += 0.002f;
                if (hue >= 1.0f) hue = 0.0f;
                repaint();
            });
            timer.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color c1 = Color.getHSBColor(hue, 0.5f, 1.0f);
            Color c2 = Color.getHSBColor(hue + 0.2f, 0.5f, 1.0f);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new InventoryPage();
    }
}
