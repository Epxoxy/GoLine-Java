package widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.WeakHashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import animator.DoubleAnimator;
import animator.IDoubleAnimated;
import common.IAction;
import ui.MaterialButton;
import widget.helpers.ComponentsHelper;

public class DialogBase implements IHost{
	private JLayeredPane topLayered;
	private TranslucencePane dialogHost;
	private JLabel dialogTitle;
	private JComponent dialogContent;
	private JPanel dialogContentPane;
	private JPanel dialogBottom;
	private JButton dialogOk;
	private JButton dialogCancel;
	private IAction onShow;
	private IAction onHide;
	private int height = 400;
	private int width = 400;
	private DoubleAnimator animator;
	private WeakHashMap<Container,Boolean> containerMap;
	private float alpha = 0.3f;
	
	public DialogBase(JLayeredPane topLayered, JComponent component){
		this.topLayered = topLayered;
		init(component);
	}
	
	public void setTransparent(float alpha){
		this.alpha = alpha;
		dialogHost.setTransparent(alpha);
		setupAnimator();
	}

	public void init(JComponent content){
		dialogHost = new TranslucencePane();
		dialogHost.setTransparent(alpha);
		dialogHost.setBackground(Color.GRAY);
		topLayered.add(dialogHost);
		topLayered.setLayer(dialogHost, 0);
		dialogHost.setLayout(new BoxLayout(dialogHost, BoxLayout.PAGE_AXIS));

		dialogContentPane = new RoundPane();
		setSize(width, height);
		dialogContentPane.setBackground(Color.WHITE);
		dialogTitle = new JLabel("Title");
		dialogTitle.setPreferredSize(new Dimension(width, 50));
		dialogTitle.setBorder(new EmptyBorder(10, 10, 0, 10));
		dialogTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
		dialogContent = content;
		dialogTitle.setHorizontalAlignment(SwingConstants.CENTER);
		dialogContentPane.setLayout(new BorderLayout(0, 0));
		dialogContentPane.add(dialogTitle, BorderLayout.NORTH);
		dialogContentPane.add(dialogContent);
		dialogContentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		dialogHost.add(dialogContentPane);
		
		dialogBottom = new JPanel();
		dialogContentPane.add(dialogBottom, BorderLayout.SOUTH);
		dialogBottom.setLayout(new GridLayout(0, 2, 0, 0));
		dialogOk = new MaterialButton("OK");
		dialogBottom.add(dialogOk);
		dialogCancel = new MaterialButton("Cancel");
		dialogBottom.add(dialogCancel);
		dialogHost.setLayout(new GridBagLayout());
		
		setupAnimator();
	}
	
	private void setupAnimator(){
		animator = new DoubleAnimator(new IDoubleAnimated(){
			@Override
			public void onAnimated(Double value) {
				float cur = value.floatValue();
				System.out.println(cur);
				dialogHost.setTransparent(cur);
				dialogHost.repaint();
			}
		},alpha, 0f, alpha, 150);
	}

	public JPanel getHost(){
		return this.dialogHost;
	}
	
	protected JComponent getContent(){
		return this.dialogContent;
	}
	
	public void setOnShowAction(IAction action){
		this.onShow = action;
	}
	
	public void setOnHideAction(IAction action){
		this.onHide = action;
	}
	
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
		dialogContentPane.setMinimumSize(new Dimension(width,height));
		dialogContentPane.setMaximumSize(new Dimension(width,height));
		dialogContentPane.setPreferredSize(new Dimension(width,height));
		dialogContentPane.setBounds(0, 0, width, height);
	}
	
	public void showDialog(String title, IAction onOk, IAction onCancel){
		clearListeners(dialogCancel);
		clearListeners(dialogOk);
		ComponentsHelper.enableComponents(dialogHost, true);
		//Initialize
		dialogTitle.setText(title);
		animator.begin();
		dialogOk.addActionListener(onhideIt);
		dialogCancel.addActionListener(onhideIt);
		if(onOk != null)
			dialogOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onOk.onAction();
			}
		});
		if(onCancel != null)
			dialogCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel.onAction();
			}
		});
		topLayered.setLayer(dialogHost, topLayered.highestLayer() + 1);
		Component[] components = topLayered.getComponents();
		containerMap = new WeakHashMap<Container,Boolean>();
		for(Component component : components){
			if(component == dialogHost) continue;
			if(component instanceof Container){
				Container container = (Container)component;
				containerMap.put(container, container.isEnabled());
				ComponentsHelper.enableComponents(container, false);
			}
		}
		if(this.onShow != null) 
			this.onShow.onAction();
	}
	
	private void clearListeners(JButton btn){
		if(btn == null) return;
		ActionListener[] listeners = btn.getActionListeners();
		for(ActionListener l : listeners){
			btn.removeActionListener(l);
		}
	}
	
	private ActionListener onhideIt = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			animator.stop().reversal().setOnCompleted(new IAction(){
				@Override
				public void onAction() {
					ComponentsHelper.enableComponents(dialogHost, false);
					animator.setOnCompleted(null).reversal();
					topLayered.setLayer(dialogHost, 0);
					if(containerMap != null && containerMap.size() > 0){
						for(Container key : containerMap.keySet()){
							if(!containerMap.get(key)) continue;
							ComponentsHelper.enableComponents(key, true);
						}
						containerMap.clear();
					}
					if(DialogBase.this.onHide != null){
						DialogBase.this.onHide.onAction();
					}
				}
			}).begin();
		}
	};

}
