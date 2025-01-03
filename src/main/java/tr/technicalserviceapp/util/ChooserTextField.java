package tr.technicalserviceapp.util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ChooserTextField extends JPopupMenu implements DocumentListener {

    private JTextField editor;
    private final List<String> list;
    private int selectedIndex = -1; // Klavye ile seçili olan öğenin indeksi

    public ChooserTextField(List<String> list) {
        this.list = list;
    }

    public void setEditor(JTextField editor) {
        if (editor != this.editor) {
            JTextField old = this.editor;
            if (old != null) {
                uninstallEditor(old);
            }
            this.editor = editor;
            if (this.editor != null) {
                installEditor(editor);
            }
        }
    }

    public void updateOptions(List<String> newOptions) {
        list.clear();
        list.addAll(newOptions);
        // Seçeneklerin yeniden çizilmesi veya güncellenmesi için gerekli işlemler yapılabilir
    }


    private void installEditor(JTextField editor) {
        this.setFocusable(false);
        editor.getDocument().addDocumentListener(this);

        // Klavye olaylarını dinle
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isShowing()) {
                    handleKeyPress(e);
                }
            }
        });


    }

    private void uninstallEditor(JTextField editor) {
        if (editor != null) {
            return;
        }
    }

    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int itemCount = this.getComponentCount();

        if (itemCount > 0) {
            if (keyCode == KeyEvent.VK_DOWN) {
                // Aşağı ok
                do {
                    selectedIndex = (selectedIndex + 1) % itemCount;
                } while (!(this.getComponent(selectedIndex) instanceof JMenuItem));
                highlightMenuItem(selectedIndex);
            } else if (keyCode == KeyEvent.VK_UP) {
                // Yukarı ok
                do {
                    selectedIndex = (selectedIndex - 1 + itemCount) % itemCount;
                } while (!(this.getComponent(selectedIndex) instanceof JMenuItem));
                highlightMenuItem(selectedIndex);
            } else if (keyCode == KeyEvent.VK_ENTER) {
                // Enter tuşu
                if (selectedIndex >= 0 && selectedIndex < itemCount) {
                    Component comp = this.getComponent(selectedIndex);
                    if (comp instanceof JMenuItem) {
                        ((JMenuItem) comp).doClick();
                    }
                }
            }
        }
    }

    private void highlightMenuItem(int index) {
        int itemCount = this.getComponentCount();
        for (int i = 0; i < itemCount; i++) {
            Component component = this.getComponent(i);
            if (component instanceof JMenuItem) {
                JMenuItem menuItem = (JMenuItem) component;
                menuItem.setArmed(i == index); // Sadece seçili öğeyi vurgula
            }
        }
    }


    private void showPopupMenu() {
        this.removeAll();
        selectedIndex = -1; // Seçimi sıfırla
        String filterText = editor.getText().trim().toLowerCase();

        // Uygun öğeleri ekle
        for (String element : list) {
            if (element.toLowerCase().startsWith(filterText)) {
                JMenuItem menuItem = new JMenuItem(element);
                menuItem.addActionListener(e -> {
                    editor.setText(element);
                    this.setVisible(false);
                });
                this.add(menuItem);
            }
        }

        // Eğer sonuç yoksa bir mesaj göster
        if (this.getComponentCount() == 0) {
            JMenuItem noResultItem = new JMenuItem("Sonuç bulunamadı");
            noResultItem.setEnabled(false);
            this.add(noResultItem);
        }

        // Dinamik boyut hesaplama
        int width = editor.getWidth();
        int height = 0;

        for (Component component : this.getComponents()) {
            Dimension preferredSize = component.getPreferredSize();
            width = Math.max(width, preferredSize.width); // En geniş öğeye göre genişliği ayarla
            height += preferredSize.height; // Yüksekliği topla
        }

        // Popup boyutunu ayarla
        this.setPopupSize(width, height);

        // Popup menüyü göster
        if (editor.isDisplayable()) {
            this.show(editor, 0, editor.getHeight());
        }
    }



    @Override
    public void insertUpdate(DocumentEvent e) {
        showPopupMenu();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        showPopupMenu();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        showPopupMenu();
    }
}
