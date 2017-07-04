package widget;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.plaf.basic.*;

import animator.ColorAnimator;
import animator.IAnimated;

import javax.swing.plaf.*;

public class CSButtonUI extends BasicButtonUI {
	protected static CSButtonUI singleton = new CSButtonUI();
	private Color rolloverForegroundColor = Color.BLACK;
	private Color rolloverBorderColor = new Color(Integer.parseInt("317EF3", 16));
	private Color rolloverBackground = new Color(Integer.parseInt("E5F1FB",16));
	private Color pressBackground = new Color(Integer.parseInt("CCE4F7",16));
	

	private static Color pressColor = new Color(Integer.parseInt("E5E5E5",16));
	private static Color rolloverColor = new Color(Integer.parseInt("CACACAC",16));
	
	private ColorAnimator rollover;
	private ColorAnimator press;
	private ColorAnimator leave;
	private ColorAnimator released;

	public static ComponentUI createUI(JComponent c) {
		IAnimated<Color> changeBg = new IAnimated<Color>(){
			@Override
			public void onAnimated(Color value) {
				c.setBackground(value);
				c.repaint();
			}
		};
		final Color bg = new Color(Integer.parseInt("EEEEEE", 16));
		System.out.println("Bg is "+bg);
		singleton.rollover = new ColorAnimator(changeBg,bg,bg,rolloverColor,200);
		singleton.press = new ColorAnimator(changeBg,bg,rolloverColor,pressColor,200);
		singleton.leave = new ColorAnimator(changeBg,bg,rolloverColor,bg,200);
		singleton.released = new ColorAnimator(changeBg,bg,pressColor,rolloverColor,200);
		c.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				singleton.press.begin();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				singleton.released.begin();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				singleton.rollover.begin();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleton.leave.begin();
			}
			
		});
		return singleton;
	}
	
	public void SetRolloverForeground(Color color){
		rolloverForegroundColor = color;
	}
	public void SetRolloverBorderColor(Color color){
		rolloverBorderColor = color;
	}
	public void SetRolloverBackground(Color color){
		rolloverBackground = color;
	}
	public void SetPressBackground(Color color){
		pressBackground = color;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		//boolean pressed = (model.isArmed() && model.isPressed()) || model.isSelected();
		boolean isRollOver = model.isRollover();
		Rectangle viewRect = new Rectangle(0, 0, b.getWidth(), b.getHeight());
		g2.setPaint(c.getBackground());
		//g2.setPaint(pressed ?  pressBackground : (isRollOver ? rolloverBackground : b.getBackground()));
		g2.fill(viewRect);
		if(isRollOver){
			//b.setBorder(BorderFactory.createLineBorder(rolloverBorderColor, 1));
			b.setForeground(rolloverForegroundColor);
		}
		else{
			//b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
			b.setForeground(b.getForeground());
		}
		super.paint(g, c);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		AbstractButton b = (AbstractButton) c;
		Dimension dim = super.getPreferredSize(c);
		dim.height += (b.getMargin().top + b.getMargin().bottom);
		dim.width += (b.getMargin().left + b.getMargin().right);
		return dim;
	}
}

