package com.patchwork.servlets;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.patchwork.db.ConnectionManager;
import com.patchwork.db.InitializeTables;
import com.patchwork.db.UserLoader;
import com.patchwork.db.security.UserValidator;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet({"/Login", "/Register","/LoginRequired", "/Logout"})
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthenticationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @see Servlet#init()
	 */
	public void init() throws ServletException {
		ConnectionManager.start();
		InitializeTables.init(this.getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher d = request.getRequestDispatcher("/WEB-INF/views/Home.jsp");
		d.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = null;
		String password = null;
		String confirm = null;
		boolean success = false;
		RequestDispatcher d = null;
		switch(request.getServletPath().toString()) {
			case "/Login":
				username = request.getParameter("username");
				password = request.getParameter("password");
				success = loginUser(request, username, password);
				if(success)
					response.sendRedirect("Lobby");
				else {
					request.setAttribute("LoginError", "Invalid Username Or Password");
					d = request.getRequestDispatcher("/WEB-INF/views/Home.jsp");
				}
				break;
			case "/Register":
				username = request.getParameter("username");
				password = request.getParameter("password");
				confirm = request.getParameter("password_confirm");
				if(UserValidator.validateNewUser(username, password, confirm)) {
					success = UserLoader.insertUser(username, password);
					if(success)
						success = loginUser(request, username, password);
					//if failed to insert user into database
					else
						request.setAttribute("RegistrationError", "Failed to Create User");
				}
				//if user exists or password error (i.e. password mismatch)
				else
					request.setAttribute("RegistrationError", "Invalid Username or Password");

				if(success)
					response.sendRedirect("Lobby");
				//if failed to login newly created user
				else {
					request.setAttribute("RegistrationError", "Failed Automatic Login");
					d = request.getRequestDispatcher("/WEB-INF/views/security/LoginCreate.jsp");
					d.forward(request, response);
				}
				break;
			case "/Logout":
				Principal user = request.getUserPrincipal();
				if(user != null) {
					try {
						request.logout();
					}catch (ServletException e){}
					d = request.getRequestDispatcher("/WEB-INF/views/Home.jsp");
					d.forward(request, response);
				}
			default:
				break;
		}
	}
	
	private boolean loginUser(HttpServletRequest request, String username, String password) {
		try {
			request.login(username, password);
		} catch (ServletException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public void destroy() {
		ConnectionManager.close();
		super.destroy();
	}

}
