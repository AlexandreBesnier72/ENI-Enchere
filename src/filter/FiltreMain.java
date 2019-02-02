package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Sert à l'initialisation des variables de session.
 * @author DWWM-Equipe #1
 */
@WebFilter("/*")
public class FiltreMain implements Filter 
{
	public FiltreMain() {}
	public void destroy() {}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest requete = (HttpServletRequest) request;
		
		HttpSession session = requete.getSession(true);
		
		if ( session.getAttribute("isConnecte") == null)
		{
			session.setAttribute("isConnecte", false);
		}
		if ( session.getAttribute("isAdministrateur") == null)
		{
			session.setAttribute("isAdministrateur", false);
		}
		if(session.getAttribute("pseudo") == null)session.setAttribute("pseudo","");
		
		session.setAttribute("erreur", "");
		session.setAttribute("succes", "");
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException
	{
		
	}
}
