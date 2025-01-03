package tr.technicalserviceapp.device;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import tr.technicalserviceapp.device.addedpart.AddedPart;
import tr.technicalserviceapp.device.addedpart.AddedParts;
import tr.technicalserviceapp.devicepart.DevicePart;
import tr.technicalserviceapp.devicepart.DeviceParts;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Locale;

public class AddedPartEditUI extends JDialog{
    private JPanel main_panel;
    private JFormattedTextField purchase_price_textfield;
    private JFormattedTextField sale_price_textfield;
    private JLabel purchase_price_label;
    private JLabel sale_price_label;
    private JLabel info_label;
    private JButton update_button;
    private JButton cancel_button;

    private AddedPart added_part;
    private DevicePart device_part;

    public AddedPartEditUI(AddedPart added_part) {
        this.added_part = added_part;
        this.device_part = DeviceParts.getDevicePart(added_part.getDevicePartID());

        add(main_panel);
        Setup();
    }

    private void Setup() {
        setTitle("Eklenen Parça İşlemleri");

        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.2);
        int height = (int) (screen_size.height * 0.3);

        setSize(width, height);
        setLocationRelativeTo(null);

        info_label.setText(device_part.getBarcode() + " - " + device_part.getName());

        JButton purchase_price_button = new JButton(new FlatSVGIcon("icon/reload.svg", 12, 12));
        purchase_price_button.addActionListener(e -> {
            double purchase_price = device_part.getPurchase_price();
            purchase_price_textfield.setValue(purchase_price);
        });
        purchase_price_textfield.putClientProperty("JTextField.trailingComponent", purchase_price_button);
        purchase_price_textfield.setValue(added_part.getPurchasePrice());

        JButton sale_price_button = new JButton(new FlatSVGIcon("icon/reload.svg", 12, 12));
        sale_price_button.addActionListener(e -> {
            double sale_price = device_part.getSale_price();
            sale_price_textfield.setValue(sale_price);
        });
        sale_price_textfield.putClientProperty("JTextField.trailingComponent", sale_price_button);
        sale_price_textfield.setValue(added_part.getSalePrice());

        cancel_button.setIcon(new FlatSVGIcon("icon/cancel.svg", 16, 16));
        cancel_button.addActionListener(e -> {
            dispose();
        });

        update_button.setIcon(new FlatSVGIcon("icon/reload.svg",16,16));
        update_button.addActionListener(e -> {
            double purchase_price = (double) purchase_price_textfield.getValue();
            double sale_price = (double) sale_price_textfield.getValue();

            added_part.setPurchasePrice(purchase_price);
            added_part.setSalePrice(sale_price);

            AddedParts.save();
            dispose();
        });
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
