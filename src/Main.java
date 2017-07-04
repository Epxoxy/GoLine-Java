import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JButton;

import core.data.AILevel;
import core.data.JudgeCode;
import core.interfaces.IBoard;
import core.interfaces.IGameCoreResolver;
import core.interfaces.JudgedLiteListener;
import core.resolver.ResolveCreator;
import ui.MaterialButton;
import widget.BoardView;
import widget.DialogBase;
import widget.DialogEx;
import widget.helpers.LayoutHelper;
import widget.helpers.SimpleJListGenerator;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import common.ColorEx;
import common.IAction;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	private JPanel cardPanel;
	private CardLayout cardLayout;
	private JPanel rootContent;
	private JLayeredPane topLayered;
	private DialogEx textDialog;
	private BoardView board;
	private DialogBase selectionDialog;
	private JList<String> modeList;
	private List<DialogBase> dialogList = new ArrayList<DialogBase>();
	private JLabel p1Label;
	private JLabel p2Label;
	
	
	private void initRootPanel(JFrame frame){
		topLayered = new JLayeredPane();
		frame.getContentPane().add(topLayered);
		rootContent = new JPanel();
		topLayered.add(rootContent);
		topLayered.setLayer(rootContent, 10);
		rootContent.setLayout(new BorderLayout(0, 0));

		textDialog = new DialogEx(topLayered);
		textDialog.setSize(300, 300);
		textDialog.setTransparent(0.7f);
		modeList = SimpleJListGenerator.build(new String[] {
				"Player vs Player", "Player vs AI", "Online"
		});
		selectionDialog = new DialogBase(topLayered, LayoutHelper.putGridBag(modeList));
		selectionDialog.setSize(300, 300);
		dialogList.add(selectionDialog);
		dialogList.add(textDialog);
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Dimension dimensionNew = topLayered.getSize();
				Rectangle rectangleNew = new Rectangle(new Point(), dimensionNew);
				for(DialogBase dialog : dialogList){
					if(dialog == null) continue;
					dialog.getHost().setBounds(rectangleNew);
				}
				rootContent.setBounds(rectangleNew);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 489, 630);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initRootPanel(frame);

		cardPanel = new JPanel();
		cardLayout = new CardLayout(0,0);
		rootContent.add(cardPanel);
		cardPanel.setLayout(cardLayout);

		JPanel welcomePanel = new JPanel();
		cardPanel.add(welcomePanel, "name_458591872886197");
		welcomePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel welTopPanel = new JPanel();
		welcomePanel.add(welTopPanel, BorderLayout.NORTH);
		
		JLabel titleLb = new JLabel("START NEW GAME");
		titleLb.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		titleLb.setBorder(new EmptyBorder(20,10,10,10));
		welTopPanel.add(titleLb);
		
		JPanel welCenterPanel = new JPanel();
		LayoutHelper.setGridBag(welCenterPanel);
		welcomePanel.add(welCenterPanel);
		
		JButton toGameBtn = new MaterialButton("start");
		toGameBtn.setFont(new Font("Segoe UI", Font.PLAIN, 50));
		toGameBtn.setPreferredSize(new Dimension(200,200));
		welCenterPanel.add(toGameBtn);
		
		JPanel welBottomPanel = new JPanel();
		welcomePanel.add(welBottomPanel, BorderLayout.SOUTH);
		
		JLabel bottomLb = new JLabel("OTHERS");
		bottomLb.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		bottomLb.setBorder(new EmptyBorder(10,10,20,10));
		welBottomPanel.add(bottomLb);
		
		JPanel gamePanel = new JPanel();
		cardPanel.add(gamePanel, "name_458593810794986");
		gamePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
		gamePanel.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		p1Label = new JLabel("P1");
		p1Label.setBorder(new EmptyBorder(10,10,10,10));
		p1Label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		p1Label.setHorizontalAlignment(SwingConstants.CENTER);
		p1Label.setBackground(Color.WHITE);
		p1Label.setOpaque(true);
		topPanel.add(p1Label);
		
		p2Label = new JLabel("P2");
		p2Label.setBorder(new EmptyBorder(10,10,10,10));
		p2Label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		p2Label.setHorizontalAlignment(SwingConstants.CENTER);
		p2Label.setOpaque(true);
		topPanel.add(p2Label);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(0,10,20,10));
		gamePanel.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton startBtn = new MaterialButton("Start");
		JButton undoBtn = new MaterialButton("Undo");
		JButton resetBtn = new MaterialButton("Reset");
		JButton backBtn = new MaterialButton("back");
		Font basicFont = new Font("Segoe UI", Font.PLAIN, 20);
		startBtn.setFont(basicFont);
		undoBtn.setFont(basicFont);
		resetBtn.setFont(basicFont);
		backBtn.setFont(basicFont);
		bottomPanel.add(startBtn);
		bottomPanel.add(undoBtn);
		bottomPanel.add(resetBtn);
		bottomPanel.add(backBtn);
		
		JPanel panel = new JPanel();
		gamePanel.add(panel);
		panel.setLayout(new CardLayout(0, 0));
		
		JPanel boardPanel = new JPanel();
		panel.add(boardPanel, "name_457402875233061");
		boardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		board = new BoardView();
		boardPanel.add(board);
		
		toGameBtn.addActionListener(showModeSelection);
		setupListener(startBtn,resetBtn,undoBtn,backBtn);
		
	}
	
	private ActionListener showModeSelection = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			selectionDialog.showDialog("Select Mode", new IAction(){
				@Override
				public void onAction() {
					int index = modeList.getSelectedIndex();
					modeList.clearSelection();
					System.out.println("*******************ModeIndex is "+index);
					cardLayout.next(cardPanel);
					setupResolver(index, board);
				}
			}, null);
		}
	};
	
	private void setupListener(JButton startBtn,JButton resetBtn, JButton undoBtn, JButton backBtn){
		//Initialize resolver
		ActionListener reset = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(resolver != null)
					resolver.reset();
			}
		};
		startBtn.addActionListener(reset);
		resetBtn.addActionListener(reset);
		undoBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(resolver != null)
					resolver.undo();
			}
		});
		backBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.next(cardPanel);
				if(resolver != null)
					resolver.dispose();
				resolver = null;
			}
		});
	}
	

	private JudgedLiteListener judgelite = new JudgedLiteListener(){
		@Override
		public void onJudged(JudgeCode code) {
			switch(code){
				case Active:{
					if(resolver.isHostActive()){
						p2Label.setBackground(p1Label.getBackground());
						p1Label.setBackground(ColorEx.ACTIVE);
					}else {
						p1Label.setBackground(p2Label.getBackground());
						p2Label.setBackground(ColorEx.ACTIVE);
					}
				}break;
				case Ended:{
					String winner = resolver.getWinnerToken();
					if(winner != null && winner.length() > 0){
						boolean hostWin = resolver.getHostToken().equals(winner);
						String text = hostWin ? "P1 WIN" : "P2 WIN";
						textDialog.showDialog("NEW WINNER", text, null, null);
					}
				}break;
				case Started:{
					
				}break;
				default:break;
			}
		}
    };
	
	private IGameCoreResolver resolver;
	private void setupResolver(int mode, IBoard board){
        System.out.println("MODE " + mode + resolver);
        switch (mode){
        	case 0:
        		resolver = ResolveCreator.BuildPVP(board);
        		break;
            case 1:
                resolver = ResolveCreator.BuildEVP(board, AILevel.Elementary);
                break;
            case 2:
                break;
            default:break;
        }
        if(resolver != null){
            System.out.println("## Resolver is OK...");
            resolver.setJudgedListener(judgelite);
            resolver.ready();
            resolver.start();
        }
    }
}
