package ai.dda;

public abstract class Dda {

	protected double health_coef;
	protected double speed_coef;
	protected int prevWavesConsideration;
	protected int dropValueCoef; 
	protected int budgetPerWave;
	
	//protected Path path_method;
	
	public Dda(){}
	
	public abstract Dda previousDda();
	
	public abstract Dda nextDda();
	
	public double getHealthCoef(){
		return health_coef;
	}
	
	public double getSpeedCoef(){
		return speed_coef;
	}
	
//	public Path getPathMethod(){
//		return path_method;
//	}
	
}
