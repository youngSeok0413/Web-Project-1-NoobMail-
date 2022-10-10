package noobmail.common;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {
	public static void log(String home, String job, String location) throws IOException{       
		LocalDate now = LocalDate.now();              
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalTime nowTime = LocalTime.now();              
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh-mm-ss");
		String time = now.format(formatter1) + "-" + nowTime.format(formatter2);        
		
		FileWriter fw = new FileWriter(home+"DB\\admin\\log.txt", true);
		fw.write("[SERVER]/"+time+"/"+job+"/"+location+"\n");
		fw.close();
	}
}
