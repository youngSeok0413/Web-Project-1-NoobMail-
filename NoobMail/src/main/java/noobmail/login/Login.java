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
		ServletContext sc = this.getServletContext();
		String homeDir = sc.getRealPath("");
		Account account  = new Account(homeDir, userId, userPassword);
		HttpSession session = req.getSession();
		
		if(proved(account)) {
			setSessionForSignIn(session, account);
			
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("main.jsp");
			requestDispatcher.forward(req, resp);
		}else {
			setSession(session);
			
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("signin.html");
			requestDispatcher.forward(req, resp);
		}
		
	}
	
	private void setSession(HttpSession session) throws IOException {
		session.setAttribute("approved", false);
		session.setAttribute("id", "");
		session.setAttribute("get", "");
		session.setAttribute("post", "");
		session.setAttribute("garbage", "");
		session.setAttribute("type", "");
	}
	
	private void setSessionForSignIn(HttpSession session, Account account) throws IOException {
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
