package widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private int round = 20;
	
	public void setRound(int round){
		this.round = round;
	}

	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		Rectangle rect = new Rectangle(0,0,getWidth(),getHeight());
		rect.grow(-2, -2);
		g2.fillRoundRect(rect.x,rect.y,rect.width,rect.height, round, round);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        //super.paintComponent(g);
    }
}
