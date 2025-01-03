package tr.cabro.compenent.suggestions;

import tr.cabro.compenent.suggestions.event.EventData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SuggestionsMenu {

    private JTextField editor;
    private final JPopupMenu menu;

    private EventData event;

    private int selectedIndex = -1;

    public SuggestionsMenu() {
        menu = new JPopupMenu();
        menu.setFocusable(false);
    }

    public void setEditor(JTextField editor) {
        if (editor != this.editor) {
            JTextField old = this.editor;
            if (old != null) {
                uninstallEditor(old);
            }
            this.editor = editor;
            if (this.editor != null) {
                installEditor();
            }
        }
    }

    private void installEditor() {
        editor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (!event.getData().isEmpty()) {
                    menu.show(editor, 0, editor.getHeight());
                    //search.clearSelected();
                }
            }
        });
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (!menu.isDisplayable()) {
                    return;
                }
                int itemCount = menu.getComponentCount();
                if (evt.getKeyCode() == KeyEvent.VK_UP) {
                    do {
                        selectedIndex = (selectedIndex - 1 + itemCount) % itemCount;
                    } while (!(menu.getComponent(selectedIndex) instanceof JMenuItem));
                    highlightMenuItem(selectedIndex);
                } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                    do {
                        selectedIndex = (selectedIndex + 1) % itemCount;
                    } while (!(menu.getComponent(selectedIndex) instanceof JMenuItem));
                    highlightMenuItem(selectedIndex);
                } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (selectedIndex >= 0 && selectedIndex < itemCount) {
                        Component comp = menu.getComponent(selectedIndex);
                        if (comp instanceof JMenuItem) {
                            ((JMenuItem) comp).doClick();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    String text = editor.getText().trim().toLowerCase();
                    setData(text);
                    if (menu.getComponentCount() > 0) {
                        //  * 2 top and bot border
                        menu.show(editor, 0, editor.getHeight());
                        menu.setPopupSize(editor.getWidth(), (menu.getComponentCount() * 35) + 2);
                    } else {
                        menu.setVisible(false);
                    }
                }
            }

            private void highlightMenuItem(int index) {
                int itemCount = menu.getComponentCount();
                for (int i = 0; i < itemCount; i++) {
                    Component component = menu.getComponent(i);
                    if (component instanceof JMenuItem) {
                        JMenuItem menuItem = (JMenuItem) component;
                        menuItem.setArmed(i == index); // Sadece seçili öğeyi vurgula
                    }
                }
            }
        });

        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = editor.getText().trim().toLowerCase();
                setData(text);
                if (menu.getComponentCount() > 0) {
                    //  * 2 top and bot border
                    menu.show(editor, 0, editor.getHeight());
                    menu.setPopupSize(editor.getWidth(), (menu.getComponentCount() * 35) + 2);
                } else {
                    menu.setVisible(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                menu.setVisible(false);
            }
        });
    }

    private void uninstallEditor(JTextField editor) {
        return;
    }

    public void addEventData(EventData event) {
        this.event = event;
    }

    private void setData(String filter) {
        menu.setVisible(false);
        menu.removeAll();
        List<Data> list = event.getData();
        for (Data data : list) {
            if (data.getText().toLowerCase().startsWith(filter)) {
                JMenuItem menuItem = new JMenuItem(data.getText());
                menuItem.addActionListener(e -> {
                    event.itemClick(data);
                    menu.setVisible(false);
                });
                menu.add(menuItem);
            }
        }
    }

    private List<Data> search(String search) {
        List<Data> list = new ArrayList<>();
        for (Data data : event.getData()) {
            String text = data.getText().toLowerCase();
            if (text.startsWith(search)) {
                list.add(data);
            }
        }
        return list;
    }
}
