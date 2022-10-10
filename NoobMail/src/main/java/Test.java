import java.io.IOException;
import noobmail.common.*;

public class Test{
	public static void main(String[] args) throws IOException {
		Account basic = new Account("src\\main\\webapp\\", "FINDER", "FINDER", "finder1234", 0);
		basic.create();
	}
}
