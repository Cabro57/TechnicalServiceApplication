package tr.technicalserviceapp.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainUI extends JFrame {
    private JPanel border;
    private JPanel background;
    private JPanel title;
    private JButton btnClose;
    private JButton btnMaximize;
    private JButton btnMinimize;

    public MainUI() {
        init();
    }

    private void init() {
        setContentPane(border);
        setSize(600,50);
        setUndecorated(true);
        setLocationRelativeTo(null);
        FlatSVGIcon close_icon = new FlatSVGIcon("icon/close.svg", 20, 20);
        FlatSVGIcon maximize_icon = new FlatSVGIcon("icon/maximize.svg", 20, 20);
        FlatSVGIcon restore_icon = new FlatSVGIcon("icon/minimize.svg", 20, 20);
        FlatSVGIcon minimize_icon = new FlatSVGIcon("icon/minus.svg", 20, 20);

        close_icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.GRAY));
        btnClose.setIcon(close_icon);
        btnClose.setBorder(new EmptyBorder(2,2,2,2));
        btnMaximize.setBorder(new EmptyBorder(2,2,2,2));
        btnMinimize.setBorder(new EmptyBorder(2,2,2,2));
        btnClose.addActionListener(e -> System.exit(0));
        btnClose.addMouseListener(ButtonHover(btnClose, Color.WHITE, Color.RED, close_icon));
        maximize_icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.GRAY));
        restore_icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.GRAY));
        btnMaximize.setIcon(maximize_icon);
        btnMaximize.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL); // Normal boyuta getir
                btnMaximize.setSelected(false);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH); // Tam ekran yap
                btnMaximize.setSelected(true);
            }
        });
        btnMaximize.setSelectedIcon(restore_icon);
        btnMaximize.addMouseListener(ButtonHover(btnMaximize, Color.GRAY, Color.WHITE, maximize_icon));
        minimize_icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.GRAY));
        btnMinimize.setIcon(minimize_icon);
        btnMinimize.addMouseListener(ButtonHover(btnMinimize, Color.GRAY, Color.WHITE, minimize_icon));
        btnMinimize.addActionListener(e ->  {
            setState(JFrame.ICONIFIED);
        });
    }

    private MouseAdapter ButtonHover(JButton editor, Color foreground, Color background, FlatSVGIcon icon) {
        return new MouseAdapter() {
            final Color currForeground = editor.getForeground();
            final Color currBackground = editor.getBackground();
            @Override
            public void mouseEntered(MouseEvent e) {
                editor.setBackground(background);
                editor.setForeground(foreground);

                // SVG simgesinin rengini değiştir
                icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> foreground));
                editor.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                editor.setBackground(currBackground);
                editor.setForeground(currForeground);

                // SVG simgesinin rengini eski haline getir
                icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> currForeground));
                editor.repaint(); // Renk değişikliğini yansıt
            }
        };
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        MainUI frame = new MainUI();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
