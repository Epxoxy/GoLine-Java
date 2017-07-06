package common;

import java.awt.Color;

public class ColorEx {
    public static final Color ACTIVE_MID = parse("#9fa8da");
    public static final Color ACTIVE = parse("#d1c4e9");
    public static final Color SKYBLUE = parse("#ceeb");
    public static final Color BLUEGREY = parse("#B0BEC5");
    public static final Color NORMALBG = parse("#fafafa");
    public static final Color ACCENT = parse("#424242");
    
    private static Color parse(String color){
    	int i = Integer.parseInt(color.substring(1), 16);
    	return new Color(i);
    }
}
