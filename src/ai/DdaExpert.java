package ai;

public class DdaExpert extends Dda{
	
	public DdaExpert(){
		health_coef = 2;
		speed_coef = 2;
		//path_method = new PathExpert();
	}

	@Override
	public Dda previousDda() {
		return new DdaHard();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaExpert();
	}
}
