package animator;

public interface IAnimatedValueCalculator<T>{
	T compute(T from, T target, double progress);
}
