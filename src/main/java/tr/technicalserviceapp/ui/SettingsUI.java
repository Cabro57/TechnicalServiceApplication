package tr.technicalserviceapp.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.google.gson.JsonObject;
import tr.technicalserviceapp.settings.Manager;
import tr.technicalserviceapp.settings.Theme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SettingsUI extends JDialog {
    private JPanel content_pane;
    private JButton ok_button;
    private JButton cancel_button;
    private JButton apply_button;
    private JComboBox<String> theme_combobox;
    private JTextField background_textfield;
    private JLabel theme_label;
    private JCheckBox full_screen_checkbox;

    private final MainUI mainUI;

    public SettingsUI(MainUI mainUI) {
        this.mainUI = mainUI;
        FlatSVGIcon icon = new FlatSVGIcon("icon/settings.svg");
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.LIGHT_GRAY));
        setIconImage(icon.getImage());
        Setup();
        setContentPane(content_pane);
        getRootPane().setDefaultButton(ok_button);

        ok_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancel_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        apply_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onApply();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        content_pane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void Setup() {
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.3);
        int height = (int) (screen_size.height * 0.5);

        setSize(width, height);
        setLocationRelativeTo(null);

        setTitle("Ayarlar");

        // Diğer ayarların yüklenmesi
        loadThemes();
    }


    private void loadThemes() {
        JsonObject themes = (JsonObject) Manager.getSetting("themes");

        if (themes != null) {
            for (String theme : themes.keySet()) {
                theme_combobox.addItem(theme);
            }
        }

        String selectedTheme = (String) Manager.getSetting("selected_theme");
        if (selectedTheme != null) {
            theme_combobox.setSelectedItem(selectedTheme);
        }

        theme_combobox.addActionListener(e -> {
            String selected_theme = (String) theme_combobox.getSelectedItem();
            if (selected_theme != null && !selected_theme.equals(Manager.getSetting("selected_theme"))) {
                Theme.apply(selected_theme);
                Manager.setSetting("selected_theme", selected_theme);
            }
        });
    }



    private void chooseBackgroundImage() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Arka Plan Resmi Seç");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG, JPG, GIF Resimleri", "png", "jpg", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            background_textfield.setText(selectedFile.getAbsolutePath());  // Dosya yolunu textfield'a yaz
            apply_button.setEnabled(true);
        }
    }


    private void onOK() {
        dispose(); // Pencereyi kapat
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onApply() {

    }
}
