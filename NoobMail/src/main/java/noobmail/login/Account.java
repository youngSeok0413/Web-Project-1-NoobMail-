package noobmail.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Account {
	
	public Account(String id, String pwd) throws IOException {
		System.out.println(getUserIdAndHash(id));
	}
	
	public void create(String id, String pwd) throws IOException{
	}
	
	public void read() {
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}
	
	private String getUserIdAndHash(String id) throws IOException {
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
