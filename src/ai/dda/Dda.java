package ai.dda;

public abstract class Dda {

	protected double health_coef;
	protected double speed_coef;
	protected int prevWavesConsideration;
	protected double dropValueCoef; 
	protected int budgetPerWave;
	protected int thresholdHP;
	protected int thresholdTime;
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
	
	public abstract String toString();

	public abstract DdaEnum getEnum();
	
//	public Path getPathMethod(){
//		return path_method;
//	}
	
}
