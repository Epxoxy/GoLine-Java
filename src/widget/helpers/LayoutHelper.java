package widget.helpers;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class LayoutHelper {
	public static JPanel putGridBag(JComponent component){
		JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		container.add(component);
		container.setBackground(Color.WHITE);
		return container;
	}
	public static void setGridBag(JPanel panel){
		panel.setLayout(new GridBagLayout());
	}
}
