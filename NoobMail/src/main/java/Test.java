import java.io.IOException;

import noobmail.common.Account;

public class Test {
	public static void main(String[] Args) throws IOException {
		Account admin2 = new Account("Me", "admin2", "1234", 999);
		Account admin1 = new Account("Me", "admin1", "1234", 999);
		admin2.delete("admin2");
	}
}
