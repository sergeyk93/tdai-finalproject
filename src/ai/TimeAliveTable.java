package ai;

import java.util.HashMap;
import java.util.Iterator;

import models.creatures.Creature;

public class TimeAliveTable {
	
	private static HashMap<String, Long> timeAliveTable;
	
	public static void init(){
		timeAliveTable = new HashMap<String, Long>();
		
		Iterator<Creature> iter = Menu.getIter();
		
		while(iter.hasNext()){
			String name = iter.next().getNom();
			timeAliveTable.put(name, (long)0);
		}
	}
	
	public static void updateAliveTime(String creatureName, long timeAlive){
		timeAliveTable.put(creatureName, timeAlive);
	}
	
	public static long getAliveTIme(String creatureName){
		return timeAliveTable.get(creatureName);
	}
}
