package ai;

public class DdaNormal extends Dda{
	
	public DdaNormal(){
			health_coef = 1;
			speed_coef = 1;
			//path_method = new PathNormal();
	}
	
	@Override
	public Dda previousDda() {
		return new DdaEasy();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaHard();
	}

}
