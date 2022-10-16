package noobmail.login;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import noobmail.common.*;

@WebServlet("/account")
public class CreateAccount extends HttpServlet{
	private String name;
	private String id;
	private String pwd;
	private String pwdconfi;
	private String home;
	private Account FINDER;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		name = req.getParameter("name");
		id = req.getParameter("id");
		pwd = req.getParameter("pwd");
		pwdconfi = req.getParameter("pwdconf");
		
		ServletContext sc = this.getServletContext();
		String home = sc.getRealPath("");
		
		FINDER = new Account(home, "FINDER", "finder1234");
		
		if(!name.equals("")&&id.equals("")
				&&pwd.equals("")&&pwdconfi.equals("")) {
			if(!whetherIDOverlapped()) {
				if(pwd.equals(pwdconfi)) {
					Account newAccount = new Account(home, name, id, pwd, 0);
					newAccount.create();
					System.out.println("Created Account");
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("welcome.html");
					requestDispatcher.forward(req, resp);
				}else {
					System.out.println("1");
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("account.html");
					requestDispatcher.forward(req, resp);
				}
			}else {
				System.out.println("2");
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("account.html");
				requestDispatcher.forward(req, resp);
			}
		}else {
			System.out.println("3");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("account.html");
			requestDispatcher.forward(req, resp);
		}
	}
	
	private boolean whetherIDOverlapped() throws FileNotFoundException, IOException {
		int ID = id.hashCode();
		
		BufferedReader br = new BufferedReader(new FileReader(FINDER.getIndexPath()));
		String str;
		while((str = br.readLine())!=null) {
			if(Integer.parseInt(str.split("/")[0]) == ID) {
				br.close();
				return true;
			}
		}
		
		br.close();
		return false;
	}
}
