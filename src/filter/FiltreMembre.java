package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Sert à une vérification d'une connexion active pour 
 * autoriser l'utilisateur connecté de pouvoir accéder à une page.
 * @author DWWM-Equipe #1
 */
@WebFilter("/membre/*")
public class FiltreMembre implements Filter {

	public FiltreMembre() {}
	public void destroy() {}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest requete = (HttpServletRequest) request;
		HttpServletResponse reponse = (HttpServletResponse) response;
		
		/* récupération de la session de la requête */
		
		HttpSession session = requete.getSession();
		
		if ( (boolean)session.getAttribute( "isConnecte") == true)
		{
			chain.doFilter(requete, reponse);
		}
		
		else
		{
			/* redirection vers une page, page de connexion*/
			
			reponse.sendRedirect(((HttpServletRequest) request).getContextPath() + "/connexion");
		}
		
		
	}

	
}
