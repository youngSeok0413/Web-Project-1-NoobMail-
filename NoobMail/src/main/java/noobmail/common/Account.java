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
	private String name;
	private String id;
	private Integer idToHash;
	private String pwd;
	private Integer value;
	private int authority;
	
	private String indexPath;
	private String mainFolderPath;
	
	public Account(String name, String id, String pwd, int authority) throws IOException {
		this.name = new String(name);
		
		this.id =new String(id);
		this.idToHash = id.hashCode();
		
		this.pwd = new String(pwd);
		this.value = (id+pwd).hashCode();
		
		this.authority = authority;
		
		this.indexPath = new String("src\\main\\webapp\\DB\\admin\\account.txt");
		if(authority == 0) {
			mainFolderPath = new String("src\\main\\webapp\\DB\\users\\"+idToHash.toString());
		}else {
			mainFolderPath = new String("src\\main\\webapp\\DB\\admin\\admins\\"+idToHash.toString());
		}
		create();
	}
	
	//create account
	public void create() throws IOException{
		if(search() == null) {
			addUserAccountToIndex();
			addAccountToDB();
		}else {
			Log.log("Failed to create account(already exists)", "src\\main\\webapp\\DB\\admin\\account.txt");
		}
	}
	//create account(add an account to index)
	private void addUserAccountToIndex() throws IOException {
		FileWriter fw = new FileWriter(new File(indexPath), true);
		
		if(authority == 0) {
			fw.write(idToHash.toString()+"/"+value.toString()+"/"+0+"\n");
			fw.close();
			Log.log("Created user account", indexPath);
		}else if(authority == 999) {
			fw.write(idToHash.toString()+"/"+value.toString()+"/"+999+"\n");
			fw.close();
			Log.log("Created admin account", indexPath);
		}
	}
	//create account(add an account to folder)
	private void addAccountToDB() throws IOException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		//'get' folder store the mails you got
		File get = new File(mainFolderPath+"\\get");
		//'post' folder store the mails you wrote
		File post = new File(mainFolderPath+"\\post");
		//'garbage' folder store the mails you deleted
		File garbage = new File(mainFolderPath+"\\garbage");
		
		if(get.mkdirs()) {
			Log.log("Created new folder", mainFolderPath+"\\get");
		}else {
			Log.log("Failed to create new folder", mainFolderPath+"\\get");
		}
		
		if(post.mkdirs()) {
			Log.log("Created new folder", mainFolderPath+"\\post");
		}else {
			Log.log("Failed to create new folder", mainFolderPath+"\\post");
		}
		
		if(garbage.mkdirs()) {
			Log.log("Created new folder", mainFolderPath+"\\garbage");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(mainFolderPath+"\\meta.txt", true));
			PrintWriter pw = new PrintWriter(bw, true);
			
			if(authority == 0) {
				pw.println(id+"/"+name+"/"+"user"+"/"+formatter.format(calendar.getTime())+"/"+0+"/"+0);
			}else{
				pw.println(id+"/"+name+"/"+"admin"+"/"+formatter.format(calendar.getTime())+"/"+0+"/"+0);
			}
			
			pw.flush();
			pw.close();
			
			Log.log("Created new file", mainFolderPath+"\\meta.txt");
		}else {
			Log.log("Failed to create new folder", mainFolderPath+"\\garbage");
		}
	}
	
	//read return user main folder
	public String read() throws IOException {
		return mainFolderPath;
	}
	
	//delete account
	public void delete() throws IOException {
		deleteDirectory(mainFolderPath);
		deleteAcoountFromIndex();
	}
	//delete directory(including sub files and directory)
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
	
	private void deleteAcoountFromIndex() throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader("src\\main\\webapp\\DB\\admin\\account.txt")
				);
		String updated = "";
		String buffer;
		while((buffer = br.readLine())!=null) {
			if(!buffer.equals(idToHash.toString()+"/"+value.toString()+"/"+authority)) {
				updated+=(buffer + "\n");
			}
		}
		
		FileWriter fw = new FileWriter("src\\main\\webapp\\DB\\admin\\account.txt");
		fw.write(updated);
		fw.close();
	}
	
	private String search() throws IOException {
		BufferedReader account = new BufferedReader(
				new FileReader(indexPath)
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
