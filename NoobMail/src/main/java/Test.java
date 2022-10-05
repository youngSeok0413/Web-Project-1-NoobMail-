import java.io.IOException;

import noobmail.common.Account;

public class Test {
	public static void main(String[] Args) throws IOException {
		Account user2 = new Account("Me", "user2", "1234", 0);
		user2.create();
	}
}
