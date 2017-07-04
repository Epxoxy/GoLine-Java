package widget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JComponent;

import animator.ScaleAnimator;
import animator.IDoubleAnimated;
import common.ColorEx;
import core.interfaces.IBoard;
import core.interfaces.LatticeClickListener;
import widget.helpers.Bound;
import widget.helpers.DrawPoint;
import widget.helpers.MarkEx;

public class BoardView extends JComponent implements IBoard{
	private static final long serialVersionUID = 1L;
	private Circle[][] circles;
	private DrawPoint[] originPoints;
	private DrawPoint[] drawingPoints;
	private int maxCoorLength;
	private float latticeLength = 40;
	private int maxRadius = 0;
	private int padding = 4;
	private Bound pathBound;
	private Bound boardBound;
	private int oldWidth = 0;
	private int oldHeight = 0;
	
	public BoardView(){
		init();
		addMouseListener(clickListener);
	}
	
	private void init(){
		pathBound = new Bound();
		boardBound = new Bound();
        StringBuilder builder = new StringBuilder();
        builder.append("M2,1 H4 M0,3 H2 M4,3 H6 M2,5 H4 M3,0 H0 V6 H3");
        builder.append(" M3,4 V6 H6 V0 H3 L5,2 V4 L3,6 L1,4 L3,2 V0 L1,2 L3,4 L5,2 H1 V4 H5 L3,2");
        this.setDrawPoints(builder.toString());
	}

    private void setDrawPoints(String markExString){
        List<DrawPoint> pointList = DrawPoint.parseDrawPoints(markExString);
        originPoints = new DrawPoint[pointList.size()];
        drawingPoints = new DrawPoint[pointList.size()];
        maxCoorLength = 0;
        for(int i = 0; i < this.originPoints.length; i++){
            originPoints[i] = pointList.get(i);
            int max = (int)Math.max(originPoints[i].x, originPoints[i].y);
            drawingPoints[i] = originPoints[i].clone();
            drawingPoints[i].x = (drawingPoints[i].x * latticeLength + pathBound.left);
            drawingPoints[i].y = (drawingPoints[i].y * latticeLength + pathBound.top);
            if(max > maxCoorLength) maxCoorLength = max;
        }
        int length = maxCoorLength + 1;
        circles = new Circle[length][length];
    }
    
    private void updateAdaptLength(int width, int height){
        //System.out.println("updateAdaptLength -> " + width + ", " + height);
        int adaptLength = 0;
        //Update Drawing rectangle
        int left = 0, right = 0;
        if(width > height){
            adaptLength = height;
            left = (width - height) / 2;
        }else{
            adaptLength = width;
            right = (height - width) / 2;
        }
        maxRadius = adaptLength / (maxCoorLength + 1);
        //Update bound
        boardBound.updateBy(left, right, adaptLength, adaptLength);
        pathBound = boardBound.recreateWithPadding(maxRadius + padding);
        //Update points
        float pathAdaptLength = adaptLength - maxRadius * 2 - padding * 2;
        latticeLength = pathAdaptLength / maxCoorLength;
        for(int i = 0; i < drawingPoints.length; i++){
            drawingPoints[i].x = originPoints[i].x * latticeLength + pathBound.left;
            drawingPoints[i].y = originPoints[i].y * latticeLength + pathBound.top;
        }
        for(int i = 0; i < circles.length; i++){
			for(int j = 0; j < circles.length; j++){
				if(circles[i][j] == null) continue;
				initCircle(i, j);
			}
		}
    }
    
    private void drawPath(DrawPoint[] points, Graphics2D g){
        if(points == null) return;
    	DrawPoint prev = null;
        for(int i = 0; i < points.length; ++i){
            DrawPoint cur = points[i];
            if(cur.mark == MarkEx.M){
            	//Jump
            }else g.drawLine((int)cur.x, (int)cur.y, (int)prev.x, (int)prev.y);
            prev = cur;
        }
    }
	
    @Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		int width = this.getWidth();
		int height = this.getHeight();
		if(oldWidth != width || oldHeight != height){
			oldWidth = width;
			oldHeight = height;
			updateAdaptLength(width, height);
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke prevStroke = g2.getStroke();
		Color prevColor = g2.getColor();
		g2.setColor(ColorEx.ACTIVE_MID);
		g2.setStroke(new BasicStroke(3.0f));
		drawPath(drawingPoints, g2);
		g2.setStroke(prevStroke);
		g2.setColor(prevColor);
		for(int i = 0; i < circles.length; i++){
			for(int j = 0; j < circles.length; j++){
				if(circles[i][j] == null) continue;
				drawCircle(g2, circles[i][j], maxRadius,maxRadius);
			}
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}

    private void initCircle(int i, int j){
		circles[i][j].x = (int) (i * latticeLength + pathBound.left);
		circles[i][j].y = (int) (j * latticeLength + pathBound.top);
    }
    
	private void drawCircle(Graphics2D g2, Circle c, int width, int height) {
		int x = c.x - c.getRadius();
		int y = c.y - c.getRadius();
		int x1 = c.x - c.getInnerR();
		int y1 = c.y - c.getInnerR();
		Ellipse2D circle = new Ellipse2D.Float();
		Rectangle2D rect2D = new Rectangle2D.Double();
		//Draw stroke
		rect2D.setFrame(newGrow(x,y,c.getD(),c.getD(), -2));
		circle.setFrame(rect2D);
		g2.setColor(c.getStroke());
		g2.fill(circle);
		//Draw fill
		rect2D.setRect(newGrow(x1,y1,c.getInnerD(),c.getInnerD(), -2));
		circle.setFrame(rect2D);
		g2.setColor(c.getFill());
		g2.fill(circle);
	}
	
	private Rectangle newGrow(int x, int y, int width, int height, int grow){
		Rectangle rect = new Rectangle(x,y,width,height);
		rect.grow(grow, grow);
		return rect;
	}

	private void addCircleImpl(int i, int j, Color fill){
		Circle circle = new Circle();
		circle.fill = fill;
		circles[i][j] = circle;
		initCircle(i, j);
		int r = circle.getRadius();
		circle.setRadius((int)(r * 1.6));
		repaint();
		new ScaleAnimator(new IDoubleAnimated(){
			private final int rad = r;
			@Override
			public void onAnimated(Double scale) {
				if(circles[i][j] != null){
					int cur = (int)(scale * rad);
					circles[i][j].setRadius(cur); 
					repaint();
				}
			}
		}, 1.6d, 1d, 100).begin();
	}
	
	private void removeCircleImpl(int i, int j){
		circles[i][j] = null;
		repaint();
	}
	
	private void removeAllCircleImpl(){
		for(int i = 0; i < circles.length; i++){
			for(int j = 0; j < circles[i].length; j++){
				circles[i][j] = null;
			}
		}
		repaint();
	}
	
	
    /**
     * Try to calculate column row value for special coordinate
     * Return int[]{column, row} if successful
     */
    private int[] calculateColumnRow(int x, int y){
        //Validate x and y coordinate is in inner board
        if (boardBound.hasInsideOf(x,y)) {
            //Calculate relative coordinate and column/row
            float relativeX = pathBound.relativeX(x);
            float relativeY = pathBound.relativeY(y);
            int column = (int)Math.rint(relativeX / latticeLength);
            int row = (int)Math.rint(relativeY / latticeLength);
            double colTarget = column * latticeLength;
            double rowTarget = row * latticeLength;
            //Validate x and y relative coordinate is in circle with special radius
            if(Math.abs(relativeX - colTarget) < maxRadius) {
                if (Math.abs(relativeY - rowTarget) < maxRadius) {
                    return new int[]{column, row };
                }
            }
        }
        return null;
    }
    private MouseListener clickListener = new MouseListener(){
    	private int radius = (new Circle()).getRadius();
    	private ScaleAnimator animator = null;
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		animator = null;
    		int x = e.getX();
    		int y = e.getY();
    		int[] data = calculateColumnRow(x, y);
    		if(data != null &&  circles[data[0]][data[1]] == null && latticClickListener != null)
    			latticClickListener.onLatticeClick(data[0], data[1], maxRadius);
    	}

    	@Override
    	public void mousePressed(MouseEvent e) {
    		int x = e.getX();
    		int y = e.getY();
    		int[] data = calculateColumnRow(x, y);
    		if(data != null && circles[data[0]][data[1]] != null){
    			animator = new ScaleAnimator(new IDoubleAnimated(){
    				@Override
    				public void onAnimated(Double scale) {
    					int cur = (int)(scale * radius);
    					circles[data[0]][data[1]].setRadius(cur); 
    					repaint();
    				}
    			},1d, 1.6d, 100);
    			animator.begin();
    		}
    	}
    	@Override
    	public void mouseReleased(MouseEvent e) {
    		if(animator != null){
    			animator.reversal();
    		}
    	}
    	@Override
    	public void mouseEntered(MouseEvent e) {}
    	@Override
    	public void mouseExited(MouseEvent e) {}
    };

    
	@Override
	public void drawChess(int x, int y, boolean host) {
		Color color = host ? common.ColorEx.SKYBLUE : common.ColorEx.BLUEGREY;
		addCircleImpl(x, y, color);
	}

	@Override
	public void removeChess(int x, int y) {
		removeCircleImpl(x, y);
	}

	@Override
	public void clearChess() {
		removeAllCircleImpl();
	}

	@Override
	public void setLatticeClickListener(LatticeClickListener listener) {
		this.latticClickListener = listener;
	}
	
	private LatticeClickListener latticClickListener;
}
