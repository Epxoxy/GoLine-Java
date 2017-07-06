package animator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import common.IAction;

public abstract class AnimatorBase<T>{
	private double progress;
	private double increment = 0.05d;
	private Timer timer;
	private IAnimated<Double> onProgress;
	protected boolean isRunning;
	private boolean isReversal;
	private IAction onCompleted;
	private IAnimatedValueCalculator<T> cal;
    private IAnimated<T> animated;

    private T origin;
    private T from;
    private T to;
    private T current;
	
	public AnimatorBase(IAnimated<T> animated, T origin, T from, T to, int interval){
		int delay = interval / (int)((1 / increment));
		if(delay <= 0) delay = 1;
		this.timer = new Timer(delay, timerListener);
    	this.animated = animated;
    	this.origin = origin;
    	this.from = from;
    	this.to = to;
    	this.cal = makeCal(this.from, this.to);
	}


	protected double getIncrement(){
		return this.increment;
	}
	
	protected void setFromTo(T from, T to){
		if(isRunning) return;
		this.from = from;
		this.to = to;
	}
	
    public T getFrom(){
    	return this.from;
    }
    
    public T getTo(){
    	return this.to;
    }
    
    public T getOrigin(){
    	return this.origin;
    }
    
    public boolean isReversal(){
    	return this.isReversal;
    }
    
	public AnimatorBase<T> begin(){
		if(!isRunning){
			progress = 0;
			isRunning = true;
			timer.start();
		}
		return this;
	}
	public AnimatorBase<T> stop(){
		if(isRunning){
			isRunning = false;
			timer.stop();
		}
		progress = 0;
		return this;
	}
	public AnimatorBase<T> pause(){
		if(isRunning){
			isRunning = false;
			timer.stop();
		}
		return this;
	}
	public AnimatorBase<T> resume(){
		if(!isRunning){
			isRunning = true;
			timer.start();
		}
		return this;
	}


	public AnimatorBase<T> reversal(){
		return reversal(true);
	}
	
	public AnimatorBase<T> reversal(boolean autoStart){
		isReversal = !isReversal;
		pause();
		progress = 1 - progress;
		T tmp = from;
		from = to;
		to = tmp;
		current = from;
		cal = makeCal(from, to);
		if(autoStart)
			begin();
		return this;
	}
	
	public AnimatorBase<T> animateOrigin(){
		stop();
		from = current;
		to = origin;
		cal = makeCal(from, to);
		begin();
		return this;
	}
	
	public AnimatorBase<T> skipToFill(){
		if(isRunning){
			stop();
			progress = 1;
			progressUpdated(1d);
		}
		return this;
	}
	
	public AnimatorBase<T> setOnCompleted(IAction onCompleted){
		this.onCompleted = onCompleted;
		return this;
	}

	protected abstract IAnimatedValueCalculator<T> makeCal(T from, T to);
	
	protected void onProgress(double progress){
		
	}
	
	private void progressUpdated(double progress){
		onProgress(progress);
		current = cal.compute(from, to, progress);
		animated.onAnimated(current);
		if(onProgress != null)
			onProgress.onAnimated(progress);
		if(progress == 1){
			timer.stop();
			isRunning = false;
			progress = 0;
			if(onCompleted != null)
				onCompleted.onAction();
		}
	}

	private ActionListener timerListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			progress += increment;
			if(progress >= 1)
				progress = 1;
			progressUpdated(progress);
		}
	};
}
