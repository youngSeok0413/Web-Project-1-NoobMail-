package noobmail.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*Account : account에 대한 것을 정의해 놓은 가장 기본적인 파일
 * */

public class Account {
	
	public Account(String name, String id, String pwd, int authority) throws IOException {
		create(name, id, pwd, authority);
	}
	
	public void create(String name, String id, String pwd, int authority) throws IOException{
		if(search(id) == null) {
			if(authority == 0) {
				addUserAccountToIndex(id, pwd, authority);
				addUserAccountToDB(name, id, pwd);
			}else if(authority == 999) {
				addUserAccountToIndex(id, pwd, authority);
				addAdminAccountToDB(name, id, pwd);
			}
		}else {
			Log.log("Failed to create account(already exists)", "src\\main\\webapp\\DB\\admin\\account.txt");
		}
	}
	
	public String read(String id) throws IOException {
		String target = search(id);
		if(target == null) {
			return null;
		}else {
			String[] parsed = target.split("/");
			if(parsed[2].equals("999")) {
				Integer idToHash = id.hashCode();
				String path = "src\\main\\webapp\\DB\\users\\"+idToHash.toString();
				return path;
			}else{
				Integer idToHash = id.hashCode();
				String path = "src\\main\\webapp\\DB\\admin\\admins\\"+idToHash.toString();
				return path;
			}
		}
	}
	
	public void delete(String id) throws IOException {
		String toDelete = search(id);
		if(toDelete.split("/")[2].equals("999")) {
			deleteDirectory("src\\main\\webapp\\DB\\admin\\admins\\"+toDelete.split("2")[0]);
		}else {
			deleteDirectory("src\\main\\webapp\\DB\\users\\"+toDelete.split("2")[0]);
		}
		deleteAcoountFromIndex(id, toDelete);
	}
	
	private void deleteDirectory(String dir) {
		File folder = new File(dir);
		if(folder.exists()) {
			File[] folder_list = folder.listFiles();
			
			for(int i = 0; i < folder_list.length; i++) {
				if(folder_list[i].isFile()) {
					folder_list[i].delete();
				}else {
					deleteDirectory(folder_list[i].getPath());
				}
			}
			
			folder.delete();
		}
	}
	
	private void deleteAcoountFromIndex(String id, String toDelete) throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader("src\\main\\webapp\\DB\\admin\\account.txt")
				);
		String updated = "";
		String buffer;
		while((buffer = br.readLine())!=null) {
			if(!buffer.equals(toDelete)) {
				updated+=(buffer + "\n");
			}
		}
		
		FileWriter fw = new FileWriter("src\\main\\webapp\\DB\\admin\\account.txt");
		fw.write(updated);
		fw.close();
	}
	
	private void addUserAccountToIndex(String id, String pwd, int authority) throws IOException {
		FileWriter fw = new FileWriter(new File("src\\main\\webapp\\DB\\admin\\account.txt"), true);
		
		Integer key = id.hashCode();
		String valstr = id+pwd;
		Integer value = valstr.hashCode();
		
		if(authority == 0) {
			fw.write(key.toString()+"/"+value.toString()+"/"+0+"\n");
			fw.close();
			Log.log("Created user account", "src\\main\\webapp\\DB\\admin\\account.txt");
		}else if(authority == 999) {
			fw.write(key.toString()+"/"+value.toString()+"/"+999+"\n");
			fw.close();
			Log.log("Created admin account", "src\\main\\webapp\\DB\\admin\\account.txt");
		}
	}
	
	private void addUserAccountToDB(String name, String id, String pwd) throws IOException {
		Integer idToHash = id.hashCode();
		String path = "src\\main\\webapp\\DB\\users\\"+idToHash.toString();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		//'get' folder store the mails you got
		File get = new File(path+"\\get");
		//'post' folder store the mails you wrote
		File post = new File(path+"\\post");
		//'garbage' folder store the mails you deleted
		File garbage = new File(path+"\\garbage");
		
		if(get.mkdirs()) {
			Log.log("Created new folder", path+"\\get");
		}else {
			Log.log("Failed to create new folder", path+"\\get");
		}
		
		if(post.mkdirs()) {
			Log.log("Created new folder", path+"\\post");
		}else {
			Log.log("Failed to create new folder", path+"\\post");
		}
		
		if(garbage.mkdirs()) {
			Log.log("Created new folder", path+"\\garbage");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(path+"\\meta.txt", true));
			PrintWriter pw = new PrintWriter(bw, true);
			pw.println(id+"/"+name+"/"+"user"+"/"+formatter.format(calendar.getTime())+"/"+0+"/"+0);
			pw.flush();
			pw.close();
			
			Log.log("Created new file", path+"\\meta.txt");
		}else {
			Log.log("Failed to create new folder", path+"\\garbage");
		}
	}
	
	private void addAdminAccountToDB(String name, String id, String pwd) throws IOException {
		Integer idToHash = id.hashCode();
		String path = "src\\main\\webapp\\DB\\admin\\admins\\"+idToHash.toString();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		//'get' folder store the mails you got
		File get = new File(path+"\\get");
		//'post' folder store the mails you wrote
		File post = new File(path+"\\post");
		//'garbage' folder store the mails you deleted
		File garbage = new File(path+"\\garbage");
				
		if(get.mkdirs()) {
			Log.log("Created new folder", path+"\\get");
		}else {
			Log.log("Failed to create new folder", path+"\\get");
			}
				
		if(post.mkdirs()) {
			Log.log("Created new folder", path+"\\post");
		}else {
			Log.log("Failed to create new folder", path+"\\post");
		}
				
		if(garbage.mkdirs()) {
			Log.log("Created new folder", path+"\\garbage");
					
			BufferedWriter bw = new BufferedWriter(new FileWriter(path+"\\meta.txt", true));
			PrintWriter pw = new PrintWriter(bw, true);
			pw.println(id+"/"+name+"/"+"admin"+"/"+formatter.format(calendar.getTime())+"/"+0+"/"+0);
			pw.flush();
			pw.close();
					
			Log.log("Created new file", path+"\\meta.txt");
		}else {
			Log.log("Failed to create new folder", path+"\\garbage");
		}
	}
	
	private String search(String id) throws IOException {
		BufferedReader account = new BufferedReader(
				new FileReader("src\\main\\webapp\\DB\\admin\\account.txt")
				);
		int idToHash = id.hashCode();
		
		String str;
		while((str = account.readLine())!=null) {
			if(Integer.parseInt(str.split("/")[0]) == idToHash) {
				account.close();
				return str;
			}
		}
		
		account.close();
		return null;
	}
}
