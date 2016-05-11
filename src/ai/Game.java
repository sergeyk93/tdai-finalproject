package ai;

public class Game {

	private static boolean smart;
    
    public Game(){}
    
    public static void init(boolean other){
    	smart = other;
    }
	
    public static boolean isSmart(){
    	return smart;
    }
}
