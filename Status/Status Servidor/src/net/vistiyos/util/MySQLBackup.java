package net.vistiyos.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.vistiyos.config.Configuration;

public class MySQLBackup {
	
	private static void transfer(InputStream input, OutputStream output) throws Exception {
		byte[] buf = new byte[1024];
		int len;
		while ((len = input.read(buf)) > 0) {
			output.write(buf, 0, len);
		}
		input.close();
		output.close();
	}
	
	public static void createBackup() throws Exception{
		String command = "mysqldump -h localhost -u "+Configuration.getValue("user")+" --password="+Configuration.getValue("password")+" -r "+System.getProperty("user.dir")+"\\backups\\back.sql "+Configuration.getValue("database")+"";
		Process child = Runtime.getRuntime().exec(command);
		InputStream input = child.getInputStream();
		FileOutputStream output = new FileOutputStream(System.getProperty("user.dir")+"\\backups\\back.sql");
		transfer(input, output);
	}

}


