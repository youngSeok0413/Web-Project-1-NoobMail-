package noobmail.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*Account : account에 대한 것을 정의해 놓은 가장 기본적인 파일
 * 파일을 읽거나 쓰거나 하는 것들은 따로 따로 구성할 것이다!!!
 * 파일을 write하는 기능은 아직 넣지는 않음(상속 받아서 구성할 것)
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
	
	//for making account
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
	}
	
	//for searching account
	public Account(String id, String pwd) throws IOException {
		this.id = new String(id);
		this.idToHash = id.hashCode();
		
		this.pwd = new String(pwd);
		this.value = (id+pwd).hashCode();
		
		this.name = new String("John Doe");
		this.authority = -1;
		
		this.indexPath = new String("src\\main\\webapp\\DB\\admin\\account.txt");
		
		String str = search();
		if(str!=null) {
			String[] parsed = str.split("/");
			String au = new String(parsed[2]);
			this.authority = Integer.valueOf(au);
			
			BufferedReader br = new BufferedReader(
					new FileReader(getMetaInfoPath()));
			String data = br.readLine();
			String[] ps = data.split("/");
			br.close();
			
			this.name = new String(ps[0]);
		}
	}
	
	//search account from index file
	public String search() throws IOException {
		BufferedReader account = new BufferedReader(
				new FileReader(indexPath)
				);
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
	
	//create account
	public void create() throws IOException{
		if(search() == null) {
			addUserAccountToIndex();
			addAccountToDB();
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
		LocalDate now = LocalDate.now();              
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalTime nowTime = LocalTime.now();              
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh-mm-ss");
		String time = now.format(formatter1) + "-" + nowTime.format(formatter2);     
		
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
				pw.println(id+"/"+name+"/"+"user"+"/"+time);
			}else{
				pw.println(id+"/"+name+"/"+"admin"+"/"+time);
			}
			
			pw.flush();
			pw.close();
			
			Log.log("Created new file", mainFolderPath+"\\meta.txt");
		}else {
			Log.log("Failed to create new folder", mainFolderPath+"\\garbage");
		}
	}
	
	//read
	//return user main folder path
	public String getMainPath() throws IOException {
		return mainFolderPath;
	}
	//return user get folder path
	public String getGetPath() throws IOException {
		return mainFolderPath+"\\get";
	}
	//return user post folder path
	public String getPostPath() throws IOException {
		return mainFolderPath+"\\post";
	}
	//return user garbage folder path
	public String getGarbagePath() throws IOException {
		return mainFolderPath+"\\garbage";
	}
	//return user garbage folder path
	public String getMetaInfoPath() throws IOException {
		return mainFolderPath+"\\meta.txt";
	}
	//return user ID
	public String getID() {
		return this.id;
	}
	//return user authority
	public String getName() {
		return this.name;
	}
	//return user authority
	public int getAuthority() {
		return this.authority;
	}
	
	//delete
	//delete account
	public void delete() throws IOException {
		LocalDate now = LocalDate.now();              
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalTime nowTime = LocalTime.now();              
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh-mm-ss");
		String time = now.format(formatter1) + "-" + nowTime.format(formatter2);
		
		deleteDirectory(mainFolderPath);
		
		deleteAcoountFromIndex();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("src\\main\\webapp\\DB\\admin"+"\\account_deleted.txt", true));
		PrintWriter pw = new PrintWriter(bw, true);
		
		if(authority == 0) {
			pw.println(id+"/"+name+"/"+"user"+"/"+time);
		}else{
			pw.println(id+"/"+name+"/"+"admin"+"/"+time);
		}
		
		pw.flush();
		pw.close();
		
		Log.log("Deleted an acoount", indexPath+"+"+mainFolderPath);
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
	//delete an account from index
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
}
