package tr.technicalserviceapp.ui;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CustomerAddUI {
    private JPanel main_panel;
    private JRadioButton individual_customer_radiobutton;
    private JRadioButton corporate_customer_radiobutton;
    private JCheckBox sorunluMüşteriCheckBox;
    private JTextField customer_name_textfield;
    private JTextField company_name_textfield;
    private JTextField id_number_textfield;
    private JTextField tax_number_textfield;
    private JTextField tax_office_textfield;
    private JTextField email_textfield;
    private JTextField phone_textfield;
    private JTextField phone2_textfield;
    private JTextField limit_textfield;
    private JTextArea address_textarea;
    private JTextArea note_textfield;
    private JButton add_button;
    private JButton all_customer_button;
    private JLabel customer_name_label;
    private JLabel company_name_label;
    private JLabel id_number_label;
    private JLabel tax_number_label;
    private JLabel tax_office_label;
    private JLabel email_label;
    private JLabel phone_label;
    private JLabel phone2_label;
    private JLabel limit_label;
    private JLabel addres_label;
    private JLabel note_label;
    private JScrollPane note_scrollpane;
    private JScrollPane address_scrollpane;

    private MainUI mainUI;

    public JPanel CustomerAddUI(MainUI mainUI) {
        this.mainUI = mainUI;
        init();
        return main_panel;

    }

    private void init() {

        // RadioButton Setup
        corporate_customer_radiobutton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // Seçim durumu kontrol ediliyor
                    company_name_textfield.setEnabled(true);
                    tax_number_textfield.setEnabled(true);
                    tax_office_textfield.setEnabled(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) { // Seçim kaldırıldıysa
                    company_name_textfield.setEnabled(false);
                    tax_number_textfield.setEnabled(false);
                    tax_office_textfield.setEnabled(false);
                }
            }
        });

        all_customer_button.addActionListener(e -> mainUI.showPanel(new CustomersUI().CustomersUI(mainUI)));
    }
}
