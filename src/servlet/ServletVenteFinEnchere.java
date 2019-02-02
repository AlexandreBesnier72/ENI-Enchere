package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.BeanArticleVendu;
import bean.BeanEnchere;
import bean.BeanUtilisateur;
import dao.DaoEncheres;
import dao.DaoException;
import dao.DaoUtilisateur;

/**
 * Servlet servant pour les fins d'enchères.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/membre/venteFinEnchere", loadOnStartup=1)
public class ServletVenteFinEnchere extends HttpServlet {
	private static final long serialVersionUID = 1L;   
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		BeanArticleVendu article;
		BeanUtilisateur utilisateurVendeur;
		BeanUtilisateur utilisateurAcheteur = new BeanUtilisateur();
		BeanEnchere enchere;
		boolean pasEnchere;
		
		HttpSession session = request.getSession();
		
		/* Récupérationde l'objet article dans Session */
		article = (BeanArticleVendu) session.getAttribute("article");
		try {
			
			enchere = DaoEncheres.derniereEnchere(article.getNoArticle());
		
			/* Récupération de lutilisateur acquereur à partir de l'enchere */
			if (enchere != null)
			{
				utilisateurAcheteur = DaoUtilisateur.utilisateurParNumero(enchere.getNoUtilisateur());
				pasEnchere = false;
			}
			else {
				pasEnchere = true;
			}
		
			request.setAttribute("pasEnchere", pasEnchere);
			
			/* Récupération de l'utilisateur vendeur à partir de l'article */
			utilisateurVendeur = article.getUtilisateur();
		
			request.setAttribute("article", article );
			request.setAttribute("utilisateurVendeur", utilisateurVendeur );
			request.setAttribute("utilisateurAcheteur", utilisateurAcheteur );
		
			request.getRequestDispatcher("/WEB-INF/membre/venteFinEnchere.jsp").forward( request,  response);	
		}
		catch (DaoException e) {
			this.envoieErreur(e.getMessage(), request, response);
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	private void envoieErreur(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("erreurDao", erreur);
        request.getRequestDispatcher("/WEB-INF/erreur/erreur.jsp").forward(request, response);
    }
}