package net.vistiyos.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	
	private static Logger logHandler = null;
	
	private static void initLog(){
		if(logHandler == null){
			try{
				logHandler = Logger.getLogger(Log.class.getName());
				FileHandler fileHandler = new FileHandler("app.log", true);        
				logHandler.addHandler(fileHandler);
			}catch(IOException ex){
				
			}
		}
	}
	
	public static void addLog(String mensaje){
		initLog();
		if(logHandler.isLoggable(Level.SEVERE)){
			logHandler.severe(mensaje);
		}
	}

}
