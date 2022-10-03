import java.io.IOException;

import noobmail.common.Account;

public class Test {
	public static void main(String[] Args) throws IOException {
		Account user1 = new Account("Me", "user1", "1234", 0);
		Account user2 = new Account("You", "user2", "1234", 0);
		Account admin1 = new Account("Me", "admin1", "1234", 999);
		Account admin2 = new Account("Me", "admin2", "1234", 999);
		
		user1.delete("user1");
	}
}
