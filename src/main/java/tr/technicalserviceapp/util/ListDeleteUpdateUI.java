package tr.technicalserviceapp.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ListDeleteUpdateUI extends JDialog{
    private JPanel main_panel;
    private JList<Name> list;
    private JTextField search_text_field;
    private JButton remove_button;
    private JButton change_button;
    private JButton add_button;

    private final CsvManager manager;
    private DefaultListModel<Name> list_model;
    String title;

    public ListDeleteUpdateUI(CsvManager manager, String title) {
        this.manager = manager;
        this.title = title;

        Setup();
        add(main_panel);
    }

    private void Setup() {
        setTitle(title);

        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen_size.width * 0.25);
        int height = (int) (screen_size.height * 0.6);

        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        search_text_field.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("icon/search.svg"));
        search_text_field.putClientProperty("JTextField.showClearButton", true);
        search_text_field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            private void filterList() {
                String searchText = search_text_field.getText().trim().toLowerCase();
                DefaultListModel<Name> filteredModel = new DefaultListModel<>();
                for (int i = 0; i < list_model.size(); i++) {
                    Name name = list_model.get(i);
                    if (name.getName().toLowerCase().contains(searchText)) {
                        filteredModel.addElement(name);
                    }
                }
                list.setModel(filteredModel);
            }
        });

        list_model = new DefaultListModel<>();
        loadNamesToList();
        list.setModel(list_model);

        add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog(ListDeleteUpdateUI.this, "Yeni isim gir:", "Ekle", JOptionPane.PLAIN_MESSAGE);
                if (newName != null && !newName.trim().isEmpty()) {
                    try {
                        manager.add(new String[]{newName});
                        loadNamesToList();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ListDeleteUpdateUI.this, "Error adding name: " + search_text_field.getText(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        change_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Name selectedValue = list.getSelectedValue();
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(ListDeleteUpdateUI.this, "Please select a name to edit.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String newName = JOptionPane.showInputDialog(ListDeleteUpdateUI.this, "İsmi güncelle:", list_model.getElementAt(selectedIndex));
                if (newName != null && !newName.trim().isEmpty()) {
                    try {
                        manager.update(selectedValue.getId(), new String[]{newName});
                        loadNamesToList();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ListDeleteUpdateUI.this, "Error editing name: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        remove_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Name selectedValue = list.getSelectedValue();
                int selectedIndex = list.getSelectedIndex();

                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(ListDeleteUpdateUI.this, "Please select a name to delete.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirmation = JOptionPane.showConfirmDialog(ListDeleteUpdateUI.this, "Are you sure you want to delete this name?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        manager.remove(selectedValue.getId());
                        loadNamesToList();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ListDeleteUpdateUI.this, "Error deleting name: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


    }

    private class Name {
        private int id;
        private String name;

        public Name(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private void loadNamesToList() {
        list_model.clear();
        try {
            List<String[]> names = manager.loadAll(); // "Name" sütunu okunur
            for (String[] name : names) {
                Name n = new Name(Integer.parseInt(name[0]), name[1]);
                list_model.addElement(n);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading names: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
