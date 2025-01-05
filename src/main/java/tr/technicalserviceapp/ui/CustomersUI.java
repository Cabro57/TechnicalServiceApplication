package tr.technicalserviceapp.ui;

import com.formdev.flatlaf.FlatClientProperties;
import raven.cell.ActionButton;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.util.concurrent.ExecutionException;

public class CustomersUI {
    private JPanel main_panel;
    private JTable customer_table;
    private JTextField table_filter_textfield;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton tümMüşterilerButton;
    private JButton new_customer_add_button;
    private JButton borcuBulunanMüşterilerButton;
    private JButton button7;
    private JButton button8;
    private JPanel table_panel;
    private JScrollPane table_scroll;
    private JScrollPane main_scroll;

    private MainUI mainUI;

    public JPanel CustomersUI(MainUI mainUI) {
        this.mainUI = mainUI;
        init();
        return main_panel;

    }

    private void init() {
        button1.putClientProperty("Button.toolbar.spacingInsets", new Insets(0,0,0,0));
        button2.putClientProperty("Button.toolbar.spacingInsets", new Insets(0,0,0,0));
        button3.putClientProperty("Button.toolbar.spacingInsets", new Insets(0,0,0,0));

        new_customer_add_button.addActionListener(e -> mainUI.showPanel(new CustomerAddUI().CustomerAddUI(mainUI)));

        customer_table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        customer_table.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:40;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");

        setupCustomerTable();
    }

    private void setupCustomerTable() {
        table_scroll.setViewportView(customer_table);

        String[] Header = { "#", "Müşteri Adı", "Firma", "Telefon Numarası", "E-Posta", "Bakiye", "İşlemler" };
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // 1. sütun String, 2. sütun Integer olacak şekilde ayarlanıyor
                switch (columnIndex) {
                    case 0:
                    case 5:
                        return Integer.class;
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(Header);

        SwingWorker<ResultSet, Object> sw = new SwingWorker<ResultSet, Object>() {
            @Override
            protected ResultSet doInBackground() throws Exception {
                try {
                    URL path = getClass().getResource("/storage/storage.db");
                    String url = "jdbc:sqlite:" + (path != null ? path.getPath() : null);

                    String query = "SELECT ID, name, business, phone, email, price FROM customers";

                    Connection conn = DriverManager.getConnection(url);
                    Statement stmt = conn.createStatement();

                    return stmt.executeQuery(query);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(customer_table, "Veritabanı bağlantı hatası.");
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ResultSet rs = get();

                    while (rs.next()) {
                        int id = rs.getInt("ID");
                        String name = rs.getString("name");
                        String business = rs.getString("business");
                        String phone = rs.getString("phone");
                        String email = rs.getString("email");
                        double price = rs.getDouble("price");
                        String process = "İşlem Yap";

                        Object[] row = {id, name, business, phone, email, price, process};

                        tableModel.addRow(row);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(customer_table, "Veritabanı bağlantı hatası.");
                } catch (NullPointerException e) {
                    System.err.println("Veritabanı Null döndürdü.");
                }
            }
        };

        sw.execute();

        customer_table.setModel(tableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        customer_table.setRowSorter(sorter);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.CENTER);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }


        };

        customer_table.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
        customer_table.getColumnModel().getColumn(5).setCellRenderer(defaultTableCellRenderer);
        customer_table.setDefaultEditor(Object.class, null);

//        customer_table.getColumn("İşlemler").setCellEditor(new ButtonEditor(new JTextField("İşlem Yap")));
//        customer_table.getColumn("İşlemler").setCellRenderer(new ButtonRenderer());

        table_filter_textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = table_filter_textfield.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null); // Filtreyi kaldır
                } else {
                    // Case-insensitive regex
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
    }
}
