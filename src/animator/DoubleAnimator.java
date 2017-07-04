package animator;

public class DoubleAnimator extends AnimatorBase<Double>{
    public DoubleAnimator(IAnimated<Double> animated, double origin, double from, double to, int interval){
    	super(animated, origin, from, to, interval);
    }
	
	@Override
	protected IAnimatedValueCalculator<Double> makeCal(Double from, Double to){
    	if(from > to ) 
    		return createDecrease();
    	else return createIncrease();
	}
	
	private IAnimatedValueCalculator<Double> createDecrease(){
		return new IAnimatedValueCalculator<Double>(){
			@Override
			public Double compute(Double from, Double target, double progress) {
				return from - (from - target) * progress;
			}
		};
	}
	
	private IAnimatedValueCalculator<Double> createIncrease(){
		return new IAnimatedValueCalculator<Double>(){
			@Override
			public Double compute(Double from, Double target, double progress) {
				return from + (target - from) * progress;
			}
		};
	}
}
