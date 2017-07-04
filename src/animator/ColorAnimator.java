package animator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorAnimator extends AnimatorBase<Color>{
	private List<Color> colors;
	
	public ColorAnimator(IAnimated<Color> animated, Color origin, Color from, Color to, int interval) {
		super(animated, origin, from, to, interval);
	}

	@Override
	protected IAnimatedValueCalculator<Color> makeCal(Color from, Color to) {
		double increment = super.getIncrement();
		if(colors != null && colors.size() > 2
				&& colors.get(0) == from && colors.get(colors.size() - 1) == to){
		}else{
			colors = makeColors(from, to, (int)(1/increment));
		}
		final List<Color> myColors = colors;
		return new IAnimatedValueCalculator<Color>(){
			private List<Color> hold = myColors;
			@Override
			public Color compute(Color from, Color target, double progress) {
				int step = (int)(progress / increment);
				System.out.println(step + ", "+ hold.get(step));
				return hold.get(step);
			}
		};
	}

	private List<Color> makeColors(Color from, Color to, int steps) {
		List<Color> colors = new ArrayList<Color>(steps + 1);
		colors.add(to);
		double total = steps;
		double rDelta = (to.getRed() - from.getRed()) / total;
		double gDelta = (to.getGreen() - from.getGreen()) / total;
		double bDelta = (to.getBlue() - from.getBlue()) / total;
		double aDelta = (to.getAlpha() - from.getAlpha()) / total;
		System.out.println(to.getRed() + ", " + to.getGreen() + ", " + to.getBlue());
		System.out.println(from.getRed() + ", " + from.getGreen() + ", " + from.getBlue());
		System.out.println(rDelta + ", " + gDelta + ", " + bDelta + ", " + aDelta);

		for (int i = 1; i < steps; i++) {
			int r = (int)(to.getRed() - (i * rDelta));
			int g = (int)(to.getGreen() - (i * gDelta));
			int b = (int)(to.getBlue() - (i * bDelta));
			int a = (int)(to.getAlpha() - (i * aDelta));
			colors.add(new Color(r, g, b, a));
		}

		colors.add(from);
		return colors;
	}

}
