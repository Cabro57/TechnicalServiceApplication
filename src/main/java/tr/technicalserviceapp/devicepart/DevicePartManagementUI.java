package tr.technicalserviceapp.devicepart;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import tr.cabro.compenent.suggestions.Data;
import tr.cabro.compenent.suggestions.SuggestionsMenu;
import tr.cabro.compenent.suggestions.event.EventData;
import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.util.ChooserTextField;
import tr.technicalserviceapp.util.CsvManager;
import tr.technicalserviceapp.util.ListDeleteUpdateUI;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DevicePartManagementUI extends JDialog {
    private JPanel main_panel;
    private JScrollPane main_scrollpane;
    private JPanel scroll_panel;

    private JPanel content_panel;
    private JLabel name_label;
    private JTextField name_textfield;
    private JLabel barcode_label;
    private JTextField barcode_textfield;
    private JLabel type_label;
    private JComboBox<String> type_combobox;
    private JLabel supplier_label;
    private JTextField supplier_textfield;
    private JLabel brand_label;
    private JTextField brand_textfield;
    private JLabel model_label;
    private JTextField model_textfield;
    private JLabel category_label;
    private JTextField category_textfield;
    private JLabel warranty_period_label;
    private JComboBox<String> warranty_period_combobox;
    private JLabel purchase_price_label;
    private JFormattedTextField purchase_price_textfield;
    private JLabel sale_price_label;
    private JFormattedTextField sale_price_textfield;
    private JLabel stock_label;
    private JSpinner stock_spinner;
    private JLabel note_label;
    private JScrollPane note_scrollpane;
    private JTextArea note_textarea;

    private JPanel buttons_panel;
    private JPanel buttons;
    private JButton add_button;
    private JButton edit_button;
    private JButton delete_button;

    private JScrollPane table_scrollpane;
    private JTable device_table;

    ChooserTextField supplier_chooser;
    ChooserTextField category_chooser;
    CsvManager supplier_csv;
    CsvManager category_csv;


    public DevicePartManagementUI() {
        setup();
        setIconImage(new FlatSVGIcon("icon/device_part.svg").getImage());
        add(main_panel);
    }

    private void setup() {

        setTitle("Cihaz İşlemleri");

        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.6);
        int height = (int) (screen_size.height * 0.6);

        setSize(width, height);
        setLocationRelativeTo(null);

        name_textfield.putClientProperty("JTextField.showClearButton", true);

        barcode_textfield.putClientProperty("JTextField.showClearButton", true);

        supplier_textfield.putClientProperty("JTextField.showClearButton", true);
        suggestionSetup(supplier_textfield, "/suppliers.csv", "Yedek Parça Tedarikçi Listesi");

        brand_textfield.putClientProperty("JTextField.showClearButton", true);

        model_textfield.putClientProperty("JTextField.showClearButton", true);

        category_textfield.putClientProperty("JTextField.showClearButton", true);
        suggestionSetup(category_textfield, "/categories.csv", "Yedek Parça Kategori Listesi");

        purchase_price_textfield.putClientProperty("JTextField.showClearButton", true);

        sale_price_textfield.putClientProperty("JTextField.showClearButton", true);

        note_textarea.putClientProperty("JTextArea.showClearButton", true);

        add_button.setIcon(new FlatSVGIcon("icon/add.svg"));
        add_button.addActionListener(e -> deviceAdd());

        edit_button.setIcon(new FlatSVGIcon("icon/edit.svg"));
        edit_button.addActionListener(e -> deviceEdit());

        delete_button.setIcon(new FlatSVGIcon("icon/delete.svg"));
        delete_button.addActionListener(e -> deviceRemove());

        DeviceParts.load();
        device_table.setModel(new DevicePartTableModel(DeviceParts.getDeviceParts()));
        device_table.getSelectionModel().addListSelectionListener(e -> deviceSelect());

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

    private void deviceAdd() {
        String name = name_textfield.getText();
        String barcode = barcode_textfield.getText();
        String type = (String) type_combobox.getSelectedItem();
        String supplier = supplier_textfield.getText();
        String brand = brand_textfield.getText();
        String model = model_textfield.getText();
        String category = category_textfield.getText();
        String warranty = (String) warranty_period_combobox.getSelectedItem();
        Double purchase_price = (Double) purchase_price_textfield.getValue();
        Double sale_price = (Double) sale_price_textfield.getValue();
        int stock = (int) stock_spinner.getValue();
        String note = note_textarea.getText();

        if (name.isEmpty() || barcode.isEmpty() || type.isEmpty() || supplier.isEmpty() || brand.isEmpty() || model.isEmpty() || category.isEmpty() || warranty.isEmpty() || note.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Lütfen tüm alanları doldurun.",
                    "Eksik Bilgi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = DeviceParts.getSafeID();
        LocalDateTime created_at = LocalDateTime.now();

        DevicePart device = new DevicePart(id, name, barcode, type);
        device.setSupplier(supplier);
        device.setBrand(brand);
        device.setModel(model);
        device.setCategory(category);
        device.setWarranty(warranty);
        device.setPurchase_price(purchase_price);
        device.setSale_price(sale_price);
        device.setStock(stock);
        device.setNote(note);
        device.setCreated_at(created_at);
        device.setUpdated_at(created_at);
        DeviceParts.add(device);

        device_table.setModel(new DevicePartTableModel(DeviceParts.getDeviceParts()));

        clearContent();
    }

    private void deviceEdit() {
        int selectedRow = device_table.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = device_table.convertRowIndexToModel(selectedRow);
            DevicePart device = DeviceParts.getDeviceParts().get(modelRow);


            String name = name_textfield.getText();
            String barcode = barcode_textfield.getText();
            String type = (String) type_combobox.getSelectedItem();
            String supplier = supplier_textfield.getText();
            String brand = brand_textfield.getText();
            String model = model_textfield.getText();
            String category = category_textfield.getText();
            String warranty = (String) warranty_period_combobox.getSelectedItem();
            Double purchase_price = (Double) purchase_price_textfield.getValue();
            Double sale_price = (Double) sale_price_textfield.getValue();
            int stock = (int) stock_spinner.getValue();
            String note = note_textarea.getText();
            LocalDateTime updated_at = LocalDateTime.now();

            device.setName(name);
            device.setBarcode(barcode);
            device.setType(type);
            device.setSupplier(supplier);
            device.setBrand(brand);
            device.setModel(model);
            device.setCategory(category);
            device.setWarranty(warranty);
            device.setPurchase_price(purchase_price);
            device.setSale_price(sale_price);
            device.setStock(stock);
            device.setNote(note);
            device.setUpdated_at(updated_at);

            DeviceParts.update(device);

            device_table.setModel(new DevicePartTableModel(DeviceParts.getDeviceParts()));

            clearContent();
        }
    }

    private void deviceRemove() {
        int selectedRow = device_table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bu müşteriyi silmek istediğinizden emin misiniz?",
                    "Silme Onayı", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int modelRow = device_table.convertRowIndexToModel(selectedRow);
                DevicePartTableModel table_model = (DevicePartTableModel) device_table.getModel();
                int devicesID = table_model.getDevicePartID(modelRow);
                DeviceParts.remove(devicesID);

                device_table.setModel(new DevicePartTableModel(DeviceParts.getDeviceParts()));

                clearContent();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir müşteri seçin.");
        }
    }

    private void clearContent() {
        name_textfield.setText("");
        barcode_textfield.setText("");
        type_combobox.setSelectedIndex(0);
        supplier_textfield.setText("");
        brand_textfield.setText("");
        model_textfield.setText("");
        category_textfield.setText("");
        warranty_period_combobox.setSelectedIndex(0);
        purchase_price_textfield.setValue(0.0);
        sale_price_textfield.setValue(0.0);
        stock_spinner.setValue(0);
        note_textarea.setText("");
    }

    private void deviceSelect() {
        int selectedRow = device_table.getSelectedRow();
        if (selectedRow != -1) {
            // Seçilen satırın modelindeki veriyi alıyoruz
            int modelRow = device_table.convertRowIndexToModel(selectedRow);
            DevicePart device = DeviceParts.getDeviceParts().get(modelRow);

            name_textfield.setText(device.getName());
            barcode_textfield.setText(device.getBarcode());
            type_combobox.setSelectedItem(device.getType());
            supplier_textfield.setText(device.getSupplier());
            brand_textfield.setText(device.getBrand());
            model_textfield.setText(device.getModel());
            category_textfield.setText(device.getCategory());
            warranty_period_combobox.setSelectedItem(device.getWarranty());
            purchase_price_textfield.setValue(device.getPurchase_price());
            sale_price_textfield.setValue(device.getSale_price());
            stock_spinner.setValue(device.getStock());
            note_textarea.setText(device.getNote());
        }
    }

    private NumberFormatter createPriceFormatter() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(new Locale("tr", "TR"));
        format.applyPattern("#,##0.00 ₺");
        NumberFormatter priceFormatter = new NumberFormatter(format);
        priceFormatter.setValueClass(Double.class);  // Fiyat değeri Double olacak
        priceFormatter.setAllowsInvalid(false);      // Geçersiz girişlere izin verme
        priceFormatter.setMinimum(0.0);              // Minimum değer 0
        priceFormatter.setMaximum(1_000_000.0);      // Maksimum değer 1.000.000
        return priceFormatter;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        NumberFormatter priceFormatter = createPriceFormatter();

        // JFormattedTextField oluştur
        purchase_price_textfield = new JFormattedTextField(priceFormatter);
        purchase_price_textfield.setFocusLostBehavior(JFormattedTextField.COMMIT);
        purchase_price_textfield.setColumns(10);
        purchase_price_textfield.setValue(0.0);

        priceFormatter = createPriceFormatter();

        sale_price_textfield = new JFormattedTextField(priceFormatter);
        sale_price_textfield.setFocusLostBehavior(JFormattedTextField.COMMIT);
        sale_price_textfield.setColumns(10);
        sale_price_textfield.setValue(0.0);
    }
}
