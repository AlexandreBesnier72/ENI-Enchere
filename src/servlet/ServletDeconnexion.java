package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet servant à la deconnexion de l'utilisateur.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/deconnexion", loadOnStartup=1)
public class ServletDeconnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session =request.getSession();
		
		session.invalidate();
		
		response.sendRedirect(request.getContextPath()+"/");
	}
}
