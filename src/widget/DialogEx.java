package widget;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import common.IAction;

public class DialogEx extends DialogBase{
	private JLabel textContent;
	
	public DialogEx(JLayeredPane topLayered){
		super(topLayered, new JLabel());
		textContent = (JLabel)super.getContent();
		textContent.setHorizontalAlignment(SwingConstants.CENTER);
		textContent.setFont(new Font("Segoe UI", Font.PLAIN, 20));
	}
	
	public void showDialog(String title, String content, IAction onOk, IAction onCancel){
		textContent.setText(content);
		super.showDialog(title, onOk, onCancel);
	}
	
}
