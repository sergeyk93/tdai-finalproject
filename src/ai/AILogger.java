package ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class AILogger {
	public static final Logger logger = Logger.getLogger(AILogger.class.getName());
	private static FileHandler fh;

	public static void initLogger() throws IOException{
		logger.setLevel(Level.INFO);
		int level = 0;
		File levelFile = new File("./src/log/level.txt");
		FileWriter fw = null;
		BufferedReader br = null;
		if(!levelFile.exists()){
			try {
				level = 1;
				levelFile.createNewFile();
				fw = new FileWriter(levelFile);
				fw.write(Integer.toString(level + 1));
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				fw.close();
			}
		}
		else{
			try {
				br = new BufferedReader(new FileReader(levelFile));
				level = Integer.parseInt(br.readLine());
				fw = new FileWriter(levelFile);
				fw.write(Integer.toString(level + 1));
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				br.close();
				fw.close();
			}
		}
		
		String logFileName = "./src/log/logger_iteration_" + level + ".log";
		
		File logFile = new File(logFileName);
		if(!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		try{
			fh = new FileHandler(logFileName, true);
			logger.addHandler(fh);
			fh.setFormatter(new SimpleFormatter());
		}
		catch(SecurityException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		logger.setUseParentHandlers(false);
		StringBuilder startingMsg = new StringBuilder();
		startingMsg.append(System.lineSeparator());
		startingMsg.append("*********************** Game Iteration #");
		startingMsg.append(level);
		startingMsg.append(" ***********************");
		startingMsg.append(System.lineSeparator());
		logger.info(startingMsg.toString());
	}
	
	public static void closeLogger(){
		fh.close();
	}
	
	public static void info(String msg){
		logger.info(msg);
	}
}
