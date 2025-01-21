package tr.technicalserviceapp.device;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import tr.cabro.compenent.suggestions.Data;
import tr.cabro.compenent.suggestions.SuggestionsMenu;
import tr.cabro.compenent.suggestions.event.EventData;
import tr.technicalserviceapp.customer.Customer;
import tr.technicalserviceapp.customer.CustomerManagementUI;
import tr.technicalserviceapp.customer.Customers;
import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.CsvManager;
import tr.technicalserviceapp.util.ListDeleteUpdateUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeviceInfoManagementUI extends JDialog {
    private JPanel main_panel;

    private JScrollPane main_scrollpane;
    private JPanel scroll_panel;

    private JPanel content_panel;
    private JLabel customer_label;
    private JTextField customer_textfield;
    private JLabel name_label;
    private JTextField name_textfield;
    private JLabel brand_label;
    private JTextField brand_textfield;
    private JLabel model_label;
    private JTextField model_textfield;
    private JLabel serial_number_label;
    private JTextField serial_number_textfield;
    private JLabel password_label;
    private JTextField password_textfield;
    private JLabel urgency_label;
    private JComboBox<String> urgency_combobox;
    private JLabel accessory_label;
    private JScrollPane accessory_scrollpane;
    private JTextArea accessory_textarea;
    private JLabel fault_label;
    private JScrollPane fault_scrollpane;
    private JTextArea fault_textarea;

    private JPanel buttons_panel;
    private JPanel buttons;
    private JButton add_button;
    private JButton edit_button;

    private JScrollPane table_scrollpane;
    private JTable operation_table;
    private JButton operation_button;

    //private CustomerChooser customer_chooser;

    private Customer customer;
    private ServiceRecord operation;

    public DeviceInfoManagementUI() {
        Setup();
        setIconImage(new FlatSVGIcon("icon/service.svg").getImage());
        main_panel.requestFocusInWindow();
        add(main_panel);

        operation_table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        operation_table.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");
    }

    private void Setup() {
        setTitle("Arızalı Cihaz Bilgileri");

        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.6);
        int height = (int) (screen_size.height * 0.6);

        setSize(width, height);
        setLocationRelativeTo(null);

        //customer_chooser = new CustomerChooser();
        SuggestionsMenu suggestion = new SuggestionsMenu();
        suggestion.setEditor(customer_textfield);
        suggestion.addEventData(new EventData() {
            @Override
            public List<Data> getData() {
                List<Data> data = new ArrayList<>();
                List<Customer> dataCsv = Customers.getCustomers();
                for (Customer s : dataCsv) {
                    data.add(new Data(s.getID(), s.getName()));
                }
                return data;
            }

            @Override
            public void itemClick(Data data) {
                customer = Customers.getCustomer(data.getID());
                customer_textfield.setText(data.getText());
            }
        });
        customer_textfield.putClientProperty("JTextField.showClearButton", true);
        customer_textfield.putClientProperty("JTextField.clearCallback",
                (Runnable) () -> {
                    customer = null;
                    customer_textfield.setText("");
                });
        JButton customer_button = new JButton(new FlatSVGIcon("icon/add.svg"));
        customer_button.addActionListener(e -> {
            JDialog dialog = new CustomerManagementUI();
            dialog.setModal(true);
            dialog.setVisible(true);
        });
        customer_textfield.putClientProperty("JTextField.trailingComponent", customer_button);

        name_textfield.putClientProperty("JTextField.showClearButton", true);
        suggestionSetup(name_textfield, "/device_name.csv", "Cihaz İsim Listesi");

        brand_textfield.putClientProperty("JTextField.showClearButton", true);
        suggestionSetup(brand_textfield, "/device_brand.csv", "Cihaz Marka Listesi");

        model_textfield.putClientProperty("JTextField.showClearButton", true);
        suggestionSetup(model_textfield, "/device_model.csv", "Cihaz Model Listesi");

        serial_number_textfield.putClientProperty("JTextField.showClearButton", true);

        password_textfield.putClientProperty("JTextField.showClearButton", true);

        add_button.setIcon(new FlatSVGIcon("icon/add.svg"));
        add_button.addActionListener(e -> addDevice());

        edit_button.setIcon(new FlatSVGIcon("icon/edit.svg"));
        edit_button.addActionListener(e -> editDevice());

        operation_button.setIcon(new FlatSVGIcon("icon/operation.svg"));
        operation_button.addActionListener(e -> operationDevice());

        ServiceRecords.load();
        operation_table.setModel(new DeviceInfoTableModel(ServiceRecords.getServiceRecords()));
        operation_table.getSelectionModel().addListSelectionListener(e -> selectDevice());

    }

    private void suggestionSetup(JTextField editor, String file_name, String title) {
        SuggestionsMenu suggestion = new SuggestionsMenu();
        suggestion.setEditor(editor);
        suggestion.addEventData(new EventData() {
            @Override
            public List<Data> getData() {
                List<Data> data = new ArrayList<>();
                try {
                    String[] header = { "ID", "name"};
                    String file_path = Manager.getPath("storage.storage_path").toString() + file_name;
                    CsvManager dataCsv = new CsvManager(header, file_path);
                    for (String[] s : dataCsv.loadAll()) {
                        data.add(new Data(Integer.parseInt(s[0]), s[1]));
                    }
                } catch (IOException e) {
                    System.err.println("Dosya yüklemesi başarısız");
                }
                return data;
            }
            @Override
            public void itemClick(Data data) {
                editor.setText(data.getText());
            }
        });
        JButton model_button = new JButton(new FlatSVGIcon("icon/more.svg"));
        model_button.addActionListener(e -> {
            try {
                String[] header = { "ID", "name"};
                String file_path = Manager.getPath("storage.storage_path").toString() + file_name;
                CsvManager dataCsv = new CsvManager(header, file_path);
                ListDeleteUpdateUI model_list = new ListDeleteUpdateUI(dataCsv, title);
                model_list.setModal(true);
                model_list.setVisible(true);
            } catch (IOException evt) {
                System.err.println("Dosya yüklemesi başarısız");
            }
        });
        editor.putClientProperty("JTextField.trailingComponent", model_button);
    }

    private void suggestionTextAdd(String text, String file_name) {
        if (text.isEmpty()) {
            return;
        }

        String[] header = { "ID", "name"};
        String file_path = Manager.getPath("storage.storage_path").toString() + file_name;
        try {
            CsvManager dataCsv = new CsvManager(header, file_path);
            for (String[] t : dataCsv.loadAll()) {
                if (t[1].equalsIgnoreCase(text)) {
                    return;
                }
            }
            dataCsv.add(new String[]{text});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDevice() {

        String customer = customer_textfield.getText();
        String name = name_textfield.getText();
        String brand = brand_textfield.getText();
        String model = model_textfield.getText();
        String serial_number = serial_number_textfield.getText();
        String password = password_textfield.getText();
        String urgency = (String) urgency_combobox.getSelectedItem();
        String accessory = accessory_textarea.getText();
        String fault = fault_textarea.getText();

        if ((this.customer == null || customer.isEmpty()) || name.isEmpty() || brand.isEmpty() || model.isEmpty() || serial_number.isEmpty() || password.isEmpty() || (urgency == null || urgency.isEmpty()) || accessory.isEmpty() || fault.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Lütfen tüm alanları doldurun.",
                    "Eksik Bilgi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        suggestionTextAdd(name, "/device_name.csv");
        suggestionTextAdd(brand, "/device_brand.csv");
        suggestionTextAdd(model, "/device_model.csv");

        int id = ServiceRecords.getSafeID();
        int customer_id = this.customer.getID();
        LocalDateTime created_at = LocalDateTime.now();

        ServiceRecord operation = new ServiceRecord(id, customer_id, name);
        operation.setBrand(brand);
        operation.setModel(model);
        operation.setSerial_number(serial_number);
        operation.setPassword(password);
        operation.setUrgency(urgency);
        operation.setAccessory(accessory);
        operation.setReported_fault(fault);
        operation.setCreated_at(created_at);
        operation.setUpdated_at(created_at);

        operation.setDetected_fault("");
        operation.setService_fee(0);
        operation.setPayment_status("Ödeme Yapılmadı");
        operation.setService_status("Tamirde");
        operation.setOrdered_part("");
        operation.setOperation_made("");
        operation.setWarranty_status(false);
        operation.setShipping_fee(0);
        operation.setDelivery_type("Elden");
        operation.setDelivery_date((LocalDateTime) null);
        operation.setWarranty_end_date((LocalDate) null);

        ServiceRecords.add(operation);

        operation_table.setModel(new DeviceInfoTableModel(ServiceRecords.getServiceRecords()));

        int rowCount = operation_table.getRowCount();
        operation_table.setRowSelectionInterval(rowCount - 1, rowCount - 1);

        clearContent();
    }

    private void editDevice() {
        int selectedRow = operation_table.getSelectedRow();
        if (selectedRow != -1) {
            String customer = customer_textfield.getText();
            int customer_id = this.customer.getID();
            String name = name_textfield.getText();
            String brand = brand_textfield.getText();
            String model = model_textfield.getText();
            String serial_number = serial_number_textfield.getText();
            String password = password_textfield.getText();
            String urgency = (String) urgency_combobox.getSelectedItem();
            String accessory = accessory_textarea.getText();
            String fault = fault_textarea.getText();
            LocalDateTime update_at = LocalDateTime.now();

            suggestionTextAdd(name, "/device_name.csv");
            suggestionTextAdd(brand, "/device_brand.csv");
            suggestionTextAdd(model, "/device_model.csv");

            operation.setCustomer(customer_id);
            operation.setName(name);
            operation.setBrand(brand);
            operation.setModel(model);
            operation.setSerial_number(serial_number);
            operation.setPassword(password);
            operation.setUrgency(urgency);
            operation.setAccessory(accessory);
            operation.setReported_fault(fault);
            operation.setUpdated_at(update_at);

            ServiceRecords.update(operation);

            operation_table.setModel(new DeviceInfoTableModel(ServiceRecords.getServiceRecords()));

            clearContent();
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen düzenlemek için bir işlem seçin.");
        }
    }

    private void operationDevice() {
        int selectedRow = operation_table.getSelectedRow();
        if (selectedRow != -1) {
            DeviceOperationManagementUI deviceOperationManagementUI = new DeviceOperationManagementUI(operation);
            deviceOperationManagementUI.setModal(true);
            deviceOperationManagementUI.setVisible(true);
            deviceOperationManagementUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    operation_table.setModel(new DeviceInfoTableModel(ServiceRecords.getServiceRecords()));
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen işlem yapmak için bir cihaz seçin.");
        }
    }

    private void selectDevice() {
        int selectedRow = operation_table.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = operation_table.convertRowIndexToModel(selectedRow);
            operation = ServiceRecords.getServiceRecords().get(modelRow);

            this.customer = Customers.getCustomer(operation.getCustomer());
            customer_textfield.setText(customer.toString());
            name_textfield.setText(operation.getName());
            brand_textfield.setText(operation.getBrand());
            model_textfield.setText(operation.getModel());
            serial_number_textfield.setText(operation.getSerial_number());
            password_textfield.setText(operation.getPassword());
            urgency_combobox.setSelectedItem(operation.getUrgency());
            accessory_textarea.setText(operation.getAccessory());
            fault_textarea.setText(operation.getReported_fault());
        }
    }

    private void clearContent() {
        customer = null;
        name_textfield.setText("");
        brand_textfield.setText("");
        model_textfield.setText("");
        serial_number_textfield.setText("");
        password_textfield.setText("");
        urgency_combobox.setSelectedIndex(0);
        accessory_textarea.setText("");
        fault_textarea.setText("");
    }

}

