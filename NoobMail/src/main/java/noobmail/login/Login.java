package noobmail.login;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import noobmail.common.*;

@WebServlet("/login")
public class Login extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("id");
		String userPassword = req.getParameter("password");
		Account account  = new Account(userId, userPassword);
		HttpSession session = req.getSession();
		
		if(proved(account)) {
			setSession(session, account);
		}else {
			//
		}
		
	}
	
	private void setSession(HttpSession session, Account account) throws IOException {
		session.setAttribute("approved", true);
		session.setAttribute("id", account.getID());
		session.setAttribute("get", account.getGetPath());
		session.setAttribute("post", account.getPostPath());
		session.setAttribute("garbage", account.getGarbagePath());
		session.setAttribute("type", account.getAuthority());
	}
	
	private Boolean proved(Account account) throws IOException {
		return (account.getAuthority()!=-1);
	}
}
