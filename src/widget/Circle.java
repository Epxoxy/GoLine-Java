package widget;
import java.awt.Color;

public class Circle{
	private int radius;
	private int d;
	private int strokeWidth;
	private int innerR;
	private int innerD;
	public int x;
	public int y;
	public Color fill = Color.DARK_GRAY;
	public Color stroke = common.ColorEx.NORMALBG;
	
	public Circle(){
		this.setRadius(24);
		this.setStrokeWidth(3);
	}
	
	public void setRadius(int radius){
		if(radius <= 0){
			this.radius = 0;
		}else{
			this.radius = radius;
		}
		innerR = this.radius - strokeWidth;
		d = this.radius * 2;
		innerD = innerR * 2;
	}
	
	public int getRadius(){
		return this.radius;
	}

	public void setStrokeWidth(int strokeWidth){
		if(strokeWidth <= 0){
			this.strokeWidth = 0;
		}else if(strokeWidth > this.radius){
			this.strokeWidth = this.radius;
		}else this.strokeWidth = strokeWidth;
		innerR = this.radius - strokeWidth;
		d = this.radius * 2;
		innerD = innerR * 2;
	}
	
	public int getStrokeWidth(){
		return this.strokeWidth;
	}
	
	public int getD(){return this.d;}
	public int getInnerD(){return this.innerD;}
	public int getInnerR(){return this.innerR;}
	public Color getFill(){return this.fill;}
	public Color getStroke(){return this.stroke;}
}
