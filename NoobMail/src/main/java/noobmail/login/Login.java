package noobmail.login;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/login")
public class Login extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("id");
		String userPassword = req.getParameter("password");
	}
	/*
	 * private bool compare(String id, String pwd) {
		String toCompare = id+pwd;
		int hash = toCompare.hashCode();
	}
	*/
}
