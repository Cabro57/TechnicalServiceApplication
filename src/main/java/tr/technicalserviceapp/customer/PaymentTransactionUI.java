package tr.technicalserviceapp.customer;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Locale;

public class PaymentTransactionUI extends JDialog{
    private JPanel main_panel;
    private JScrollPane main_scroll;
    private JPanel scroll_main_panel;
    private JComboBox<String> payment_type_combobox;
    private JFormattedTextField amount_textfield;
    private JTextArea comment_textarea;
    private JScrollPane textarea_scrollpane;
    private JButton cancel_button;
    private JButton get_payment_button;
    private JPanel button_panel;
    private JLabel payment_type_label;
    private JLabel amount_label;
    private JLabel comment_label;

    private final Customer customer;
    private final String customer_name;
    private final double customer_amount;

    public PaymentTransactionUI(Customer customer) {
        this.customer = customer;
        this.customer_name = customer.getName();
        this.customer_amount = customer.getPrice();
        PaymentTransactions.load();

        Setup();

        add(main_panel);
    }

    private void Setup() {
        setTitle(customer_name + " Toplam Borcu: " + customer_amount);

        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.3);
        int height = (int) (screen_size.height * 0.4);

        setSize(width, height);
        setLocationRelativeTo(null);

        cancel_button.addActionListener(e -> dispose());
        get_payment_button.addActionListener(e -> getPayment());
    }

    private void getPayment() {
        String payment_type = (String) payment_type_combobox.getSelectedItem();
        double amount = ((Number) amount_textfield.getValue()).doubleValue();
        String comment = comment_textarea.getText();

        PaymentTransactions.load();

        int ID = PaymentTransactions.getSafeID();
        PaymentTransaction payment_transaction = new PaymentTransaction(ID);
        payment_transaction.setCustomer_id(customer.getID());
        payment_transaction.setPayment_type(payment_type);
        payment_transaction.setAmount(amount);
        payment_transaction.setNote(comment);
        payment_transaction.setDate(LocalDate.now());

        if (payment_type != null && payment_type.equals("Borç Ekle")) {
            customer.addPrice(amount);
        } else {
            customer.removePrice(amount);
        }

        PaymentTransactions.add(payment_transaction);
        Customers.save();

        dispose();
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
        amount_textfield = new JFormattedTextField(priceFormatter);
        amount_textfield.setFocusLostBehavior(JFormattedTextField.COMMIT);
        amount_textfield.setColumns(10);
        amount_textfield.setValue(0);
    }
}
