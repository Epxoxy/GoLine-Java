package widget;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TranslucencePane extends JPanel {
	private static final long serialVersionUID = 1L;
	private float alpha = 0.75f;
	public TranslucencePane() {
        setOpaque(false);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }
	
	public void setTransparent(float alpha) {
		this.alpha = alpha;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));

        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
}
