package animator;

public class ScaleAnimator extends DoubleAnimator{

	public ScaleAnimator(IDoubleAnimated animated, Double from, Double to, int interval) {
		super(animated, 1d,from, to, interval);
	}
}
