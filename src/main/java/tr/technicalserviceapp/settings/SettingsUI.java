package tr.technicalserviceapp.settings;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.google.gson.JsonObject;
import tr.technicalserviceapp.MainUI;

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
    private JLabel background_label;
    private JCheckBox full_screen_checkbox;

    private final MainUI mainUI;

    public SettingsUI(MainUI mainUI) {
        this.mainUI = mainUI;
        setIconImage(new FlatSVGIcon("icon/settings.svg").getImage());
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
        int width = (int) (screen_size.width * 0.6);
        int height = (int) (screen_size.height * 0.6);

        setSize(width, height);
        setLocationRelativeTo(null);

        setTitle("Ayarlar");

        // Arka plan seçim toolbar
        JToolBar toolbar = new JToolBar();
        JButton background_button = new JButton(new FlatSVGIcon("icon/folder.svg"));
        toolbar.add(background_button);
        background_button.addActionListener(e -> chooseBackgroundImage());

        // Arka plan yolu yükle
        String background_path = (String) Manager.getSetting("background_image");
        background_textfield.setText(background_path);
        background_textfield.putClientProperty("JTextField.trailingComponent", background_button);
        background_textfield.putClientProperty("JTextField.showClearButton", true);
        background_textfield.putClientProperty("JTextField.clearCallback",
                (Runnable) () -> {
                    background_textfield.setText("");
                    apply_button.setEnabled(true);
                });

        // Full screen checkbox ayarı
        full_screen_checkbox.setSelected(Boolean.parseBoolean((String) Manager.getSetting("full_screen")));
        full_screen_checkbox.addActionListener(e -> apply_button.setEnabled(true)); // Değişiklik olduğunda apply aktif

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

    private void setBackgroundImage() {
        String backgroundImagePath = background_textfield.getText();

        if (!backgroundImagePath.isEmpty() && !backgroundImagePath.equals(Manager.getSetting("background_image"))) {
            Manager.setSetting("background_image", backgroundImagePath);
            mainUI.setBackground(new ImageIcon(backgroundImagePath).getImage());
        } else if (backgroundImagePath.isEmpty()) {
            Manager.setSetting("background_image", "");
            mainUI.setBackground((Image) null);
        }

        apply_button.setEnabled(false);
    }

    private void saveFullScreenSetting() {
        boolean isSelected = full_screen_checkbox.isSelected(); // Checkbox durumunu al
        String currentSetting = (String) Manager.getSetting("full_screen");

        // Eğer mevcut ayar farklıysa değişikliği kaydet
        if (currentSetting == null || !currentSetting.equals(Boolean.toString(isSelected))) {
            Manager.setSetting("full_screen", Boolean.toString(isSelected)); // Yeni değeri kaydet
            mainUI.setFullScreen(isSelected); // MainUI üzerinden full screen ayarını uygula
        }

        apply_button.setEnabled(false); // Apply butonunu pasifleştir
    }


    private void onOK() {
        setBackgroundImage(); // Arka plan ayarını kaydet
        saveFullScreenSetting(); // Full screen ayarını kaydet
        dispose(); // Pencereyi kapat
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onApply() {
        setBackgroundImage(); // Arka plan ayarını uygula
        saveFullScreenSetting(); // Full screen ayarını uygula
    }
}
