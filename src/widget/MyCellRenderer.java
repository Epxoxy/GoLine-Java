package widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import common.ColorEx;

public class MyCellRenderer<T> extends JLabel implements ListCellRenderer<T> {
	private static final long serialVersionUID = 1L;
	private int top;
	private int left;
	private int bottom;
	private int right;
	
	public MyCellRenderer(int top, int left, int bottom, int right) {
		setOpaque(true);
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}
	public MyCellRenderer() {
		this(16,80,16,80);
	}
	
	public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus){
    	 setText(value.toString());
    	 setHorizontalAlignment(JLabel.CENTER);
    	 setFont(new Font("Segoe UI", Font.PLAIN, 16));

         setBackground(isSelected ? ColorEx.ACTIVE_MID : Color.white);
         setForeground(isSelected ? Color.white : Color.black);
         this.setBorder(new EmptyBorder(top,left,bottom,right));
         return this;
     }
 }
