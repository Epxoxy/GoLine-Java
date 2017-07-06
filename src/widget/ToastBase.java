package widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import animator.DoubleAnimator;
import animator.IDoubleAnimated;
import common.ColorEx;
import common.IAction;
import ui.MaterialButton;
import widget.helpers.ComponentsHelper;

public class ToastBase implements IHost{
	private JLayeredPane topLayered;
	private JPanel toastHost;
	private RoundPane toastLayer;
	private JLabel toastContent;
	private JPanel toastEast;
	private JButton actionBtn;
	private IAction onShow;
	private IAction onHide;
	private int height = 48;
	private int width = 400;
	private DoubleAnimator animator;
	private boolean isShowing;
	private int delay = 3000;
	private Stack<String> contents;
	private Stack<IAction> actions;
	private Timer timer;

	public ToastBase(JLayeredPane topLayered){
		this(topLayered, true);
	}
	
	public ToastBase(JLayeredPane topLayered, boolean top){
		this(topLayered, top, null);
	}
	
	public ToastBase(JLayeredPane topLayered, boolean top, JButton actionBtn){
		this.topLayered = topLayered;
		this.actionBtn = actionBtn;
		init(top);
	}

	public void init(boolean isInTop){
		contents = new Stack<String>();
		actions = new Stack<IAction>();
		initLabel();
		toastHost = new JPanel();
		toastHost.setOpaque(false);
		toastLayer = new RoundPane();
		toastLayer.setBackground(ColorEx.ACCENT);
		if(isInTop)
			toastLayer.notRoundTop(true);
		else toastLayer.notRoundBottom(true);
		toastLayer.setLayout(new BorderLayout(0,0));
		toastLayer.setBorder(new EmptyBorder(10, 10, 10, 10));
		toastHost.add(toastLayer);
		setSize(width, height);

		toastEast = new JPanel();
		toastEast.setLayout(new GridLayout(0, 1, 0, 0));
		if(actionBtn != null){
			actionBtn = new MaterialButton("OK");
			toastEast.add(actionBtn);
		}
		toastLayer.add(toastContent, BorderLayout.CENTER);
		toastLayer.add(toastEast, BorderLayout.EAST);
		
		toastHost.setLayout(new BoxLayout(toastHost, BoxLayout.Y_AXIS));
		topLayered.add(toastHost);
		topLayered.setLayer(toastHost, topLayered.lowestLayer() - 1);
		
		setupAnimator();
	}
	
	private void initLabel(){
		toastContent = new JLabel();
		toastContent.setForeground(Color.WHITE);
		toastContent.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	}
	
	private void setupAnimator(){
		animator = new DoubleAnimator(new IDoubleAnimated(){
			@Override
			public void onAnimated(Double value) {
				float cur = value.floatValue();
				System.out.println(cur);
				Point p = toastLayer.getLocation();
				p.y = -(int)(toastLayer.getHeight() * (1 -cur))-2;
				toastLayer.setLocation(p);
				toastLayer.repaint();
			}
		},1, 0f, 1, 150);
	}

	public JPanel getHost(){
		return this.toastHost;
	}
	
	protected JComponent getContent(){
		return this.toastContent;
	}
	
	public void setOnShowAction(IAction action){
		this.onShow = action;
	}
	
	public void setDelay(int delay){
		this.delay = delay;
	}
	
	public int getDelay(){
		return this.delay;
	}
	
	public void setOnHideAction(IAction action){
		this.onHide = action;
	}
	
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
		toastLayer.setMinimumSize(new Dimension(width,height));
		toastLayer.setMaximumSize(new Dimension(width,height));
		toastLayer.setPreferredSize(new Dimension(width,height));
		toastLayer.setBounds(0, 0, width, height);
	}
	
	
	private void ensure(){
		if(!isShowing && !contents.isEmpty()){
			isShowing = true;
			String content = contents.pop();
			IAction action = actions.pop();
			clearListeners(actionBtn);
			toastContent.setText(content);
			ComponentsHelper.enableComponents(toastHost, true);
			//Initialize
			animator.begin();
			if(actionBtn != null){
				actionBtn.addActionListener(onCompleted);
				if(action != null)
					actionBtn.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						action.onAction();
					}
				});
			}
			timer = new Timer(delay, onCompleted);
			timer.setRepeats(false);
			timer.start();
			topLayered.setLayer(toastHost, topLayered.highestLayer() + 1);
			if(this.onShow != null) 
				this.onShow.onAction();
		}
	}
	
	public void showToast(String content){
		showToast(content, null);
	}
	
	public void showToast(String content, IAction onAction){
		contents.push(content);
		actions.push(onAction);
		ensure();
	}
	
	private void clearListeners(JButton btn){
		if(btn == null) return;
		ActionListener[] listeners = btn.getActionListeners();
		for(ActionListener l : listeners){
			btn.removeActionListener(l);
		}
	}

	private ActionListener onCompleted = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			if(isShowing){
				animator.stop().setOnCompleted(new IAction(){
					@Override
					public void onAction() {
						if(isShowing){
							ComponentsHelper.enableComponents(toastHost, false);
							animator.setOnCompleted(null).reversal(false);
							isShowing = false;
							topLayered.setLayer(toastHost, 0);
							if(ToastBase.this.onHide != null){
								ToastBase.this.onHide.onAction();
							}
							ensure();
						}
					}
				}).reversal();
			}
		}
	};
}
