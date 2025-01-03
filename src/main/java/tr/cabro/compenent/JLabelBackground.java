package tr.cabro.compenent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class JLabelBackground extends JLabel {
    Image image;

    public JLabelBackground(ImageIcon imageIcon) {
        setBackground(imageIcon.getImage());
    }

    public JLabelBackground(Image image) {
        setBackground(image);
    }

    public JLabelBackground() {
        setBackground();
    }

    public void setBackground() {
        // Varsayılan resmi yükle
        URL url = getClass().getResource("/background.jpg");
        if (url != null) {
            setBackground(new ImageIcon(url).getImage());
        } else {
            System.err.println("Varsayılan arka plan resmi bulunamadı!");
            this.image = null; // Alternatif olarak boş bırakabilirsiniz.
        }
    }

    public void setBackground(Image image) {
        if (image != null) {
            this.image = image;
            repaint(); // Yalnızca resim değiştiğinde bileşeni yeniden çiz
        } else {
            System.err.println("Hata");
        }
    }

    /**
     * Resmi yüksek kaliteli ölçeklendirme ile boyutlandır.
     *
     * @param image   Orijinal resim
     * @param width   Hedef genişlik
     * @param height  Hedef yükseklik
     * @return Yüksek kaliteli yeniden boyutlandırılmış resim
     */
    private Image getHighQualityScaledImage(Image image, int width, int height) {
        // BufferedImage oluştur
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // Yüksek kaliteli render ayarları
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Resmi yeniden boyutlandır ve çiz
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return resizedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            int width = getWidth();
            int height = getHeight();

            // Eğer genişlik ve yükseklik sıfırdan büyükse ölçekleme uygula
            if (width > 0 && height > 0) {
                Image scaledImage = getHighQualityScaledImage(image, width, height);
                g.drawImage(scaledImage, 0, 0, null);
            }
        }
    }
}
