package tr.technicalserviceapp.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.nio.file.Path;

public class MainUI extends JFrame {
    private JPanel main_panel;
    private JPanel button_panel;
    private JButton home_button;
    private JButton service_button;
    private JButton device_part_button;
    private JButton customer_button;
    private JButton settings_button;
    private JPanel content_panel;
    private JButton supplier_button;

    public MainUI() {
        init();
    }

    private void init() {
        getRootPane().setDefaultButton(home_button);
        URL path = getClass().getResource("/icon/icon.png");
        setIconImage(new ImageIcon(path != null ? path.getPath() : null).getImage());
        setTitle("Servicio - Servis Takip Programı");
        setContentPane(main_panel);
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.6);
        int height = (int) (screen_size.height * 0.6);
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        home_button.setIcon(new FlatSVGIcon("icon/home.svg", 32, 32).setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY)));
        service_button.setIcon(new FlatSVGIcon("icon/service.svg", 32, 32).setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY)));
        device_part_button.setIcon(new FlatSVGIcon("icon/device_part.svg", 32, 32).setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY)));
        customer_button.setIcon(new FlatSVGIcon("icon/customer.svg", 32, 32).setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY)));
        supplier_button.setIcon(new FlatSVGIcon("icon/supplier.svg", 32, 32).setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY)));
        settings_button.setIcon(new FlatSVGIcon("icon/settings.svg", 32, 32).setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY)));

        home_button.putClientProperty("JButton.squareSize", true);
        settings_button.putClientProperty("JButton.squareSize", true);

        home_button.addActionListener(e -> showPanel(new HomeUI().HomeUI()));
        setupServiceButton();
        setupDevicePartButton();
        setupCustomerButton();
        setupSupplierButton();
        settings_button.addActionListener(e -> {
            SettingsUI settingsui = new SettingsUI(this);
            settingsui.setModal(true);
            settingsui.setVisible(true);
        });

        content_panel.setLayout(new BorderLayout());
        content_panel.add(new HomeUI().HomeUI());

    }

    protected void showPanel(JPanel panel) {
        content_panel.removeAll(); // Önceki tüm panelleri kaldır
        content_panel.add(panel); // Yeni paneli ekle
        content_panel.revalidate(); // Bileşen düzenini yeniden hesapla
        content_panel.repaint(); // Paneli yeniden çiz
    }

    private void setupCustomerButton() {
        customer_button.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            menu.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background: $Button.background"
            );

            JMenuItem itemCustomerAdd = new JMenuItem("Müşteri ve Bayi Ekle");
            JMenuItem itemCustomers = new JMenuItem("Müşteriler ve Bayiler");
            JMenuItem itemCustomerTransfer = new JMenuItem("Excel Müşteri Aktar");

            itemCustomerAdd.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemCustomers.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemCustomerTransfer.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");

            itemCustomerAdd.addActionListener(e1 -> showPanel(new CustomerAddUI().CustomerAddUI(MainUI.this)));
            itemCustomers.addActionListener(e1 -> showPanel(new CustomersUI().CustomersUI(MainUI.this)));

            menu.add(itemCustomerAdd);
            menu.add(itemCustomers);
            menu.add(itemCustomerTransfer);

            menu.show(customer_button, -1, 0);
            menu.setPopupSize(customer_button.getWidth(), (menu.getComponentCount() * customer_button.getHeight()) + 2);
        });
    }

    private void setupDevicePartButton() {
        device_part_button.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            menu.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background: $Button.background"
            );

            JMenuItem itemDevicePartAdd = new JMenuItem("Ürün Ekle");
            JMenuItem itemDeviceParts = new JMenuItem("Ürünler");
            JMenuItem itemDevicePartHistory = new JMenuItem("Ürün Geçmişi");
            JMenuItem itemDevicePartTransfer = new JMenuItem("Excel Ürün Aktar");

            itemDevicePartAdd.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemDeviceParts.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemDevicePartHistory.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemDevicePartTransfer.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");

            itemDevicePartAdd.addActionListener(e1 -> showPanel(new CustomerAddUI().CustomerAddUI(MainUI.this)));
            itemDeviceParts.addActionListener(e1 -> showPanel(new CustomersUI().CustomersUI(MainUI.this)));

            menu.add(itemDevicePartAdd);
            menu.add(itemDeviceParts);
            menu.add(itemDevicePartHistory);
            menu.add(itemDevicePartTransfer);

            menu.show(device_part_button, -1, 0);
            menu.setPopupSize(device_part_button.getWidth(), (menu.getComponentCount() * device_part_button.getHeight()) + 2);
        });
    }

    private void setupServiceButton() {
        service_button.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            menu.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background: $Button.background"
            );

            JMenuItem itemServiceAdd = new JMenuItem("Servis Kaydı Oluştur");
            JMenuItem itemServiceFollow = new JMenuItem("Arızalı Cihaz Takibi");
            JMenuItem itemDeviceName = new JMenuItem("Cihaz Adları Yönetimi");
            JMenuItem itemBrandName = new JMenuItem("Marka Adları Yönetimi");

            itemServiceAdd.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemServiceFollow.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemDeviceName.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemBrandName.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");

            itemServiceAdd.addActionListener(e1 -> showPanel(new CustomerAddUI().CustomerAddUI(MainUI.this)));
            itemServiceFollow.addActionListener(e1 -> showPanel(new CustomersUI().CustomersUI(MainUI.this)));

            menu.add(itemServiceAdd);
            menu.add(itemServiceFollow);
            menu.add(itemDeviceName);
            menu.add(itemBrandName);

            menu.show(service_button, -1, 0);
            menu.setPopupSize(service_button.getWidth(), (menu.getComponentCount() * service_button.getHeight()) + 2);
        });
    }

    private void setupSupplierButton() {
        supplier_button.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            menu.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background: $Button.background"
            );

            JMenuItem itemSupplierAdd = new JMenuItem("Tedarikçi Ekle");
            JMenuItem itemSuppliers = new JMenuItem("Tüm Tedarikçiler");
            JMenuItem itemSupplierTransfer = new JMenuItem("Excel Tedarikçi Aktar");

            itemSupplierAdd.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemSuppliers.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");
            itemSupplierTransfer.putClientProperty(FlatClientProperties.STYLE, "" +
                    "selectionBackground: $PopupMenu.background");

            itemSupplierAdd.addActionListener(e1 -> showPanel(new CustomerAddUI().CustomerAddUI(MainUI.this)));
            itemSuppliers.addActionListener(e1 -> showPanel(new CustomersUI().CustomersUI(MainUI.this)));

            menu.add(itemSupplierAdd);
            menu.add(itemSuppliers);
            menu.add(itemSupplierTransfer);

            menu.show(supplier_button, -1, 0);
            menu.setPopupSize(supplier_button.getWidth(), (menu.getComponentCount() * supplier_button.getHeight()) + 2);
        });
    }

}
