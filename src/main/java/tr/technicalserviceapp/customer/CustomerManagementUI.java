package tr.technicalserviceapp.customer;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

public class CustomerManagementUI extends JDialog {
    private JPanel main_panel;

    private JPanel content_panel;
    private JLabel name_label;
    private JTextField name_textfield;
    private JLabel idnumber_label;
    private JFormattedTextField idnumber_textfield;
    private JLabel email_label;
    private JTextField email_textfield;
    private JLabel phone_label;
    private JTextField phone_textfield;
    private JLabel status_label;
    private JComboBox<String> status_combobox;
    private JFormattedTextField price_textfield;
    private JLabel adress_label;
    private JScrollPane address_scrollpane;
    private JTextArea address_textarea;
    private JLabel price_label;
    private JScrollPane note_scrollpane;
    private JTextArea note_textarea;

    private JPanel buttons_panel;
    private JPanel buttons;
    private JButton add_button;
    private JButton edit_button;
    private JButton delete_button;

    private JTable customer_table;
    private JScrollPane table_scrollpane;
    private JLabel note_label;

    private Customer customer;

    public CustomerManagementUI() {
        setup();
        setIconImage(new FlatSVGIcon("icon/customer.svg").getImage());
        add(main_panel);
    }

    private void setup() {
        setTitle("Müşteri İşlemleri");

        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.6);
        int height = (int) (screen_size.height * 0.6);

        setSize(width, height);
        setLocationRelativeTo(null);

        name_textfield.putClientProperty("JTextField.showClearButton", true);

        idnumber_textfield.putClientProperty("JTextField.showClearButton", true);
        idnumber_textfield.setColumns(11);

        email_textfield.putClientProperty("JTextField.showClearButton", true);

        phone_textfield.putClientProperty("JTextField.showClearButton", true);

        JButton price_button = new JButton(new FlatSVGIcon("icon/payment.svg", 16, 16));
        price_button.addActionListener(e -> {
            if (customer != null) {
                PaymentTransactionUI payment = new PaymentTransactionUI(customer);
                payment.setModal(true);
                payment.setVisible(true);
                payment.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                payment.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        customer_table.setModel(new CustomerTableModel(Customers.getCustomers()));
                    }
                });
            }
        });
        price_textfield.putClientProperty("JTextField.trailingComponent", price_button);

        address_textarea.putClientProperty("JTextArea.showClearButton", true);

        note_textarea.putClientProperty("JTextArea.showClearButton", true);

        add_button.setIcon(new FlatSVGIcon("icon/add.svg"));
        add_button.addActionListener(e -> customerAdd());

        edit_button.setIcon(new FlatSVGIcon("icon/edit.svg"));
        edit_button.addActionListener(e -> customerEdit());

        delete_button.setIcon(new FlatSVGIcon("icon/delete.svg"));
        delete_button.addActionListener(e -> customerRemove());

        Customers.load();
        customer_table.setModel(new CustomerTableModel(Customers.getCustomers()));
        customer_table.getSelectionModel().addListSelectionListener(e -> customerSelect());
    }

    private void customerAdd() {
        String name = name_textfield.getText();
        String id_number = idnumber_textfield.getText();
        String email = email_textfield.getText();
        String phone_no = phone_textfield.getText().replaceAll(" ", "");
        String status = (String) status_combobox.getSelectedItem();
        Double price = ((Number) price_textfield.getValue()).doubleValue();
        String address = address_textarea.getText();
        String note = note_textarea.getText();

        if (name.isEmpty() || id_number.isEmpty() || phone_no.isEmpty() || (status == null || status.isEmpty()) || address.isEmpty() || note.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Lütfen tüm alanları doldurun.",
                    "Eksik Bilgi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Customers.getSafeID();
        LocalDateTime created_at = LocalDateTime.now();

        Customer customer = new Customer(id, name);
        customer.setId_number(id_number);
        customer.setEmail(email);
        customer.setPhone_number(phone_no);
        customer.setStatus(status);
        customer.setPrice(price);
        customer.setAddress(address);
        customer.setNote(note);
        customer.setCreated_at(created_at);
        customer.setUpdated_at(created_at);
        Customers.add(customer);

        customer_table.setModel(new CustomerTableModel(Customers.getCustomers()));

        clearContent();
    }

    private void customerEdit() {
        int selectedRow = customer_table.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = customer_table.convertRowIndexToModel(selectedRow);
            Customer customer = Customers.getCustomers().get(modelRow);

            String name = name_textfield.getText();
            String id_number = idnumber_textfield.getText();
            String email = email_textfield.getText();
            String phone_no = phone_textfield.getText().replaceAll("[^0-9]", "").replaceAll("^90", "");
            String status = (String) status_combobox.getSelectedItem();
            double price = ((Number) price_textfield.getValue()).doubleValue();
            String address = address_textarea.getText();
            String note = note_textarea.getText();
            LocalDateTime updated_at = LocalDateTime.now();

            customer.setName(name);
            customer.setId_number(id_number);
            customer.setEmail(email);
            customer.setPhone_number(phone_no);
            customer.setStatus(status);
            customer.setPrice(price);
            customer.setAddress(address);
            customer.setNote(note);
            customer.setUpdated_at(updated_at);

            Customers.update(customer);

            customer_table.setModel(new CustomerTableModel(Customers.getCustomers()));

            clearContent();
        }
    }

    private void customerRemove() {
        int selectedRow = customer_table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bu müşteriyi silmek istediğinizden emin misiniz?",
                    "Silme Onayı", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int modelRow = customer_table.convertRowIndexToModel(selectedRow);
                CustomerTableModel table_model = (CustomerTableModel) customer_table.getModel();
                int customerID = table_model.getCustomerID(modelRow);
                Customers.remove(customerID);

                customer_table.setModel(new CustomerTableModel(Customers.getCustomers()));

                clearContent();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir müşteri seçin.");
        }
    }

    private void customerSelect() {
        int selectedRow = customer_table.getSelectedRow();
        if (selectedRow != -1) {
            // Seçilen satırın modelindeki veriyi alıyoruz
            int modelRow = customer_table.convertRowIndexToModel(selectedRow);
            customer = Customers.getCustomers().get(modelRow);

            // TextField'lara seçilen satırdaki verileri yazdırıyoruz
            name_textfield.setText(customer.getName());
            idnumber_textfield.setText(customer.getId_number());
            email_textfield.setText(customer.getEmail());
            phone_textfield.setText(customer.getPhone_number());
            status_combobox.setSelectedItem(customer.getStatus());
            price_textfield.setValue(customer.getPrice());
            address_textarea.setText(customer.getAddress());
            note_textarea.setText(customer.getNote());
        }
    }

    private void clearContent() {
        name_textfield.setText("");
        idnumber_textfield.setText("");
        email_textfield.setText("");
        phone_textfield.setText("");
        status_combobox.setSelectedItem("");
        price_textfield.setValue(0);
        address_textarea.setText("");
        note_textarea.setText("");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(new Locale("tr", "TR"));
        format.applyPattern("#,##0.00 ₺");
        NumberFormatter priceFormatter = new NumberFormatter(format);
        priceFormatter.setValueClass(Double.class);  // Fiyat değeri Double olacak
        priceFormatter.setAllowsInvalid(false);      // Geçersiz girişlere izin verme
        priceFormatter.setMinimum(0.0);              // Minimum değer 0
        priceFormatter.setMaximum(1_000_000.0);      // Maksimum değer 1.000.000

        // JFormattedTextField oluştur
        price_textfield = new JFormattedTextField(priceFormatter);
        price_textfield.setFocusLostBehavior(JFormattedTextField.COMMIT);
        price_textfield.setColumns(10);
        price_textfield.setValue(0);

        try {
            MaskFormatter phone_formatter = new MaskFormatter("0### ### ## ##");
            phone_textfield = new JFormattedTextField(phone_formatter);
            phone_textfield.setColumns(10);

        } catch (ParseException e) {
            phone_textfield = new JFormattedTextField();
            throw new RuntimeException(e);
        }
    }
}
