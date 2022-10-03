package noobmail.common;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	public static void log(String job, String location) throws IOException{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		FileWriter fw = new FileWriter("src\\main\\webapp\\DB\\admin\\log.txt", true);
		fw.write("[SERVER]/"+calendar.getTime()+"/"+job+"/"+location+"\n");
		fw.close();
	}
}
