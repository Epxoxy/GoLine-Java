package widget;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;

	public CustomButton(String text) {
		super(text);
		this.setUI(CSButtonUI.createUI(this));
		this.setForeground(Color.DARK_GRAY);
		this.setMargin(new Insets(10,10, 10, 10));
		this.setContentAreaFilled(true);
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
	}

	public CustomButton() {
		this("");
	}
}