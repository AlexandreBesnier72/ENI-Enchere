package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.BeanArticleVendu;
import bean.BeanRetrait;
import bean.BeanUtilisateur;
import dao.DaoException;
import dao.DaoRetrait;

/**
 * Servlet servant à la synchronisation entre les nouveaux 
 * propriétaire des articles et les enchères.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/membre/acquisition", loadOnStartup=1)
public class ServletAquisition extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DaoRetrait daoRetrait = new DaoRetrait();
		BeanRetrait retrait = new BeanRetrait();
		BeanArticleVendu article = new BeanArticleVendu();
		BeanUtilisateur utilisateur = new BeanUtilisateur();
		
		HttpSession session =(HttpSession) request.getSession();
		
		/* Récupérationde l'objet article dans Session */
		article = (BeanArticleVendu) session.getAttribute("article");
		
		/* Récupération de l'objet retrait lié à l'article */
		try 
		{
			retrait = daoRetrait.obtenirRetrait(article.getNoArticle());		
		
			/* Récupération de l'objet utlisateur lié à l'article */
			utilisateur = article.getUtilisateur();
			
			request.setAttribute("article", article );
			request.setAttribute("retrait", retrait );
			request.setAttribute( "utilisateur" , utilisateur);
			
			request.getRequestDispatcher("/WEB-INF/membre/acquisition.jsp").forward( request,  response);
		}
		catch (DaoException e) 
		{			
			this.envoieErreur( e.getMessage(), request,  response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	private void envoieErreur(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("erreurDao", erreur);
        request.getRequestDispatcher("/WEB-INF/erreur/erreur.jsp").forward(request, response);
    }
}
