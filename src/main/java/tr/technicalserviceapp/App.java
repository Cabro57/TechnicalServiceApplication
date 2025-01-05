package tr.technicalserviceapp;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args) {

        FlatDarculaLaf.setup();

        UIManager.put( "ScrollBar.trackArc", 999 );
        UIManager.put( "ScrollBar.thumbArc", 999 );
        UIManager.put( "ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ) );
        UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Sistem tepsisi destekleniyor mu kontrolü
                if (!SystemTray.isSupported()) {
                    System.out.println("Sistem tepsisi desteklenmiyor.");
                    return;
                }

                SystemTray systemTray = SystemTray.getSystemTray();

                // Tepsi simgesi için bir resim yükleme
                String path = getClass().getResource("/icon/icon.png").getPath();
                System.out.println(path);
                Image iconImage = new ImageIcon(path).getImage(); // İkon dosyasının yolu

                // Popup menü oluşturma
                PopupMenu popupMenu = new PopupMenu();

                // Menü öğeleri ekleme
                MenuItem openItem = new MenuItem("Aç");
                MenuItem exitItem = new MenuItem("Çıkış");

                popupMenu.add(openItem);
                popupMenu.addSeparator(); // Menüye ayırıcı ekler
                popupMenu.add(exitItem);

                // Tepsi simgesi oluşturma
                TrayIcon trayIcon = new TrayIcon(iconImage, "Servicio", popupMenu);

                // İkonun otomatik ölçeklenmesine izin ver
                trayIcon.setImageAutoSize(true);

                // Sistem tepsisine ikon ekleme
                try {
                    systemTray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println("Sistem tepsisine ikon eklenemedi.");
                    e.printStackTrace();
                    return;
                }

                // "Aç" menü öğesi tıklandığında yapılacak işlem
                openItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Örneğin bir pencere açma
                        JOptionPane.showMessageDialog(null, "Uygulama açıldı!");
                    }
                });

                // "Çıkış" menü öğesi tıklandığında yapılacak işlem
                exitItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Sistem tepsisinden simgeyi kaldır ve programı kapat
                        systemTray.remove(trayIcon);
                        System.exit(0);
                    }
                });

            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new tr.technicalserviceapp.ui.MainUI();
                frame.setVisible(true);
            }
        });
    }
}
