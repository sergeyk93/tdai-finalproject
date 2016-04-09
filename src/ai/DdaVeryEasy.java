package ai;

public class DdaVeryEasy extends Dda{
	
	public DdaVeryEasy(){
		health_coef = 0.5;
		speed_coef = 0.5;
		//path_method = new PathVeryEasy();
	}

	@Override
	public Dda previousDda() {
		return new DdaVeryEasy();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaEasy();
	}

}
