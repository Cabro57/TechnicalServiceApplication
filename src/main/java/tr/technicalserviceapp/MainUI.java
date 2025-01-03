package tr.technicalserviceapp;

import tr.technicalserviceapp.customer.CustomerManagementUI;
import tr.technicalserviceapp.devicepart.DevicePartManagementUI;
import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.device.DeviceInfoManagementUI;
import tr.technicalserviceapp.settings.SettingsUI;
import tr.cabro.compenent.JLabelBackground;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class MainUI extends JFrame {
    private JPanel main_panel;
    private JPanel header_panel;
    private JPanel header_content_panel;
    private JButton operation_registry_button;
    private JButton customer_operation;
    private JButton device_operation;
    private JButton settings_button;
    private JButton close_button;
    private JPanel body_panel;
    private JLabelBackground background;

    public MainUI() {
        Setup();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(main_panel);
    }

    public void Setup() {
        boolean b = Boolean.parseBoolean((String) Manager.getSetting("full_screen"));
        setFullScreen(b);



        operation_registry_button.addActionListener(e -> showUI(new DeviceInfoManagementUI()));
        customer_operation.addActionListener(e -> showUI(new CustomerManagementUI()));
        device_operation.addActionListener(e -> showUI(new DevicePartManagementUI()));
        settings_button.addActionListener(e -> showUI(new SettingsUI(this)));
        close_button.addActionListener(e -> closeApplication());

        Path path = Manager.getPath("background_image");
        if (path != null) {
            try {
                URL url = path.toUri().toURL();
                background.setBackground(new ImageIcon(url).getImage());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            dispose();
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
            close_button.setVisible(true);
        } else {
            dispose();
            setUndecorated(false);
            Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screen_size.width * 0.8);
            int height = (int) (screen_size.height * 0.8);
            setSize(width, height);
            setMinimumSize(new Dimension(width, height));
            setLocationRelativeTo(null);
            setVisible(true);
            close_button.setVisible(false);
        }
    }


    private void showUI(JDialog dialog) {
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    public void setBackground(Image image) {
        if (image != null) {
            background.setBackground(image);
        } else {
            background.setBackground();
        }
    }

    private void closeApplication() {
        Object[] options = {"Evet", "Hayır"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Uygulamayı kapatmak istiyor musunuz?",
                "Onay",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // Varsayılan simge (null)
                options, // Özelleştirilmiş buton metinleri
                options[0] // Varsayılan seçim ("Evet")
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
