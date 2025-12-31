package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MonopolyButton extends JButton {
    private Color baseColor = new Color(34, 139, 34); // سبز مونوپولی
    private Color hoverColor = new Color(50, 205, 50); // سبز روشن‌تر برای هاور

    public MonopolyButton(String text) {
        super(text);
        setContentAreaFilled(false); // غیرفعال کردن ظاهر پیش‌فرض
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Vazirmatn", Font.BOLD, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // افکت تغییر رنگ هنگام رفتن موس روی دکمه
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
            }
        });

        setBackground(baseColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // رسم پس‌زمینه گرد
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // رسم یک خط دور (Border) فانتزی
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 20, 20);

        super.paintComponent(g2);
        g2.dispose();
    }
}

