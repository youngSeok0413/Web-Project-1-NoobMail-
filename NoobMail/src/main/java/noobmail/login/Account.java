package noobmail.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Account {
	
	public Account(String name, String id, String pwd, int authority) throws IOException {
		create(name, id, pwd, authority);
	}
	
	public void create(String name, String id, String pwd, int authority) throws IOException{
		if(search(id) == null) {
			if(authority == 0) {
				addUserAccountToIndex(id, pwd);
				addUserAccountToDB(name, id, pwd);
			}else if(authority == 999) {
				addUserAccountToIndex(id, pwd);
				addAdminAccountToDB(name, id, pwd);
			}
		}
	}
	
	public void read() {
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}
	
	private void addUserAccountToIndex(String id, String pwd) throws IOException {
		FileWriter fw = new FileWriter(new File("src\\main\\webapp\\DB\\admin\\account.txt"));
		
		Integer key = id.hashCode();
		String valstr = id+pwd;
		Integer value = valstr.hashCode();
		
		fw.write(key.toString()+"/"+value.toString());
		
		fw.close();
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
			System.out.println("Successed to make new folder: "+idToHash.toString()+"/"+"get");
		}else {
			System.out.println("Failed to make new folder: "+idToHash.toString()+"/"+"get");
		}
		
		if(post.mkdirs()) {
			System.out.println("Successed to make new folder: "+idToHash.toString()+"/"+"post");
		}else {
			System.out.println("Failed to make new folder: "+idToHash.toString()+"/"+"post");
		}
		
		if(garbage.mkdirs()) {
			System.out.println("Successed to make new folder: "+idToHash.toString()+"/"+"garbage");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path+"\\meta.txt")));
			bw.write(id+"/"+name+"/"+"user"+"/"+formatter.format(calendar.getTime())+"/"+0+"/"+0);
			
			bw.flush();
			bw.close();
		}else {
			System.out.println("Failed to make new folder: "+idToHash.toString()+"/"+"garbage");
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
			System.out.println("Successed to make new folder: "+idToHash.toString()+"/"+"get");
		}else {
			System.out.println("Failed to make new folder: "+idToHash.toString()+"/"+"get");
		}
		
		if(post.mkdirs()) {
			System.out.println("Successed to make new folder: "+idToHash.toString()+"/"+"post");
		}else {
			System.out.println("Failed to make new folder: "+idToHash.toString()+"/"+"post");
		}
		
		if(garbage.mkdirs()) {
			System.out.println("Successed to make new folder: "+idToHash.toString()+"/"+"garbage");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path+"\\meta.txt")));
			bw.write(id+"/"+name+"/"+"admin"+"/"+formatter.format(calendar.getTime())+"/"+0+"/"+0);
			
			bw.flush();
			bw.close();
		}else {
			System.out.println("Failed to make new folder: "+idToHash.toString()+"/"+"garbage");
		}
	}
	
	private String search(String id) throws IOException {
		BufferedReader account = new BufferedReader(
				new FileReader("src\\main\\webapp\\DB\\admin\\account.txt")
				);
		
		String str;
		while((str = account.readLine())!=null) {
			String[] set = str.split("/");
			if(set[0].equals(id)) {
				account.close();
				return str;
			}
		}
		
		account.close();
		return null;
	}
}
