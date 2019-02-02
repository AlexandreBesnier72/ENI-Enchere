package servlet;

import bean.BeanArticleVendu;
import bean.BeanEnchere;
import bean.BeanRetrait;
import bean.BeanUtilisateur;
import dao.DaoArticlesVendus;
import dao.DaoEncheres;
import dao.DaoException;
import dao.DaoRetrait;
import dao.DaoUtilisateur;
import metier.SecuriteVente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet servant à envoyer une nouvelle enchère.
 * @author DWWM-Equipe
 */
@WebServlet(urlPatterns = "/afficherEnchere", loadOnStartup = 1)
public class ServletEnchere extends HttpServlet
{
   
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String erreur;

        // dao
        DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
        DaoEncheres daoEncheres = new DaoEncheres();
        DaoArticlesVendus daoArticlesVendus = new DaoArticlesVendus();

        // bean
        BeanArticleVendu articleVendu = (BeanArticleVendu) session.getAttribute("article");
        BeanEnchere enchere = new BeanEnchere();
        BeanUtilisateur utilisateur = null;
        try
        {
	        utilisateur = daoUtilisateur.utilisateurParPseudo( (String) session.getAttribute("pseudo") );
	       
	        BeanEnchere derniereEnchere = null;
	       
	        derniereEnchere = daoEncheres.derniereEnchere( articleVendu.getNoArticle() );
	     
	        // récupère le montant du formulaire
	        int montantEnchere = Integer.valueOf( request.getParameter("montantEnchere") );
	
	        // vérifie le montant
	        erreur = SecuriteVente.verifMontantEnchere(montantEnchere, articleVendu, utilisateur);

	        // recrédite l'encherriseur précédent
	        if (derniereEnchere != null)
	        {
	            daoUtilisateur.recrediterUtilisateur( derniereEnchere );
	        }
	
	        // créer la nouvelle enchère
	        enchere.setNoUtilisateur( utilisateur.getNoUtilisateur() );
	        enchere.setNoArticle( articleVendu.getNoArticle() );
	        enchere.setDateEnchere( new Timestamp(System.currentTimeMillis()) );
	        enchere.setMontantEnchere(montantEnchere);
	
	        if (erreur == null)
	        {
	            // envoie la nouvelle enchère	            
	                daoEncheres.nouvelleEnchere(enchere);	           
	
	            // débite les crédits du nouvel enchérisseur	           
	                daoUtilisateur.debiterUtilisateur(utilisateur, enchere.getMontantEnchere());	           
	
	            // met à jour l'article	            
	                daoArticlesVendus.modifierPrixArticle(articleVendu.getNoArticle(), enchere.getMontantEnchere());
	           
	            session.setAttribute("succes", "Enchère réussie");
	            response.sendRedirect(request.getContextPath() + "/");
	        }
	        else
	        {
	        	session.setAttribute("erreur", erreur);
	        	this.doGet(request, response);

	        }
    	}   
        catch (DaoException e)
        {
            this.envoieErreur(e.getMessage(), request, response);
        }
    }

    /**
     * Affiche un objet en enchère.
     * @param request Requête.
     * @param response Réponse.
     * @throws ServletException Erreur de Servlet.
     * @throws IOException Exception survenue lors de l'ouverture ou de la fermeture d'un fichier.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        DaoArticlesVendus daoArticlesVendus = new DaoArticlesVendus();
		DaoRetrait daoRetrait = new DaoRetrait();
		DaoEncheres doaEnchere = new DaoEncheres();
		DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
		BeanArticleVendu articleVendu = null;
		BeanRetrait retrait = null;
		BeanEnchere enchere = null;

		try
		{
			if (request.getParameter("article") != null)
			{
				int noArticle = Integer.valueOf( request.getParameter("article") );
				/* Récupération de l'objet retrait */
				retrait = daoRetrait.obtenirRetrait(noArticle);
				session.setAttribute("retrait", retrait);
	
				articleVendu = daoArticlesVendus.articleParNumero(noArticle);
	
				String pseudoUtilisateur = (String) session.getAttribute("pseudo");
				String pseudoVendeur = null;
				String pseudoDerniereEnchere = null;
	
				// Récupération du pseudo de l'utilisateur qui à fait la dernière enchère
				enchere  = doaEnchere.derniereEnchere( noArticle);
	
				if(enchere != null)
				{
					pseudoDerniereEnchere = (daoUtilisateur.utilisateurParNumero(enchere.getNoUtilisateur())).getPseudo();
				}
	
				pseudoVendeur = daoUtilisateur.utilisateurParNumero(articleVendu.getNoUtilisateur()).getPseudo();
	
				Date dateCourante = new Date();
				Date dateExpiration = null;
				Date dateDebut = null;
				boolean enchereEnCours = false;
				boolean enchereNonCommence = false;
	
				request.setAttribute("dateCourante", dateCourante);
	
				// parsage de la date
				try
				{
					dateExpiration = this.dateFormat.parse( articleVendu.getDateFinEncheres().toString() );
					dateDebut = this.dateFormat.parse( articleVendu.getDateDebutEncheres().toString() );
				}
				catch (ParseException e)
				{
					System.err.println("Parsage des dates échouées.");
				}
	
				// Test date pour affichage bouton modifier
				if ( dateCourante.compareTo(dateDebut) < 0 )
				{
					enchereNonCommence = true;
				}
				// envois du test date bouton expiration
				request.setAttribute("enchereNonCommence", enchereNonCommence);
	
				// Test date pour affichage bouton enchérir
				if (dateCourante.compareTo(dateExpiration) < 0 && dateDebut.compareTo(dateCourante) < 0 )
				{
					enchereEnCours = true;
				}
				// envois du test date bouton expiration
				request.setAttribute("enchereEnCours", enchereEnCours);
	
				session.setAttribute("article", articleVendu);
	
				// date expirer et l'article appartient à l'utilisateur connecté
				if (dateCourante.compareTo(dateExpiration) > 0 && pseudoUtilisateur.equals(pseudoVendeur))
				{
					response.sendRedirect(request.getContextPath() + "/membre/venteFinEnchere");
				}
	
				// date expirer et l'utilisateur qui a fais la dernière enchère correspond à l'utilisateur connecté
				else if (dateCourante.compareTo(dateExpiration) > 0 && pseudoUtilisateur.equals(pseudoDerniereEnchere))
				{
					response.sendRedirect(request.getContextPath() + "/membre/acquisition");
				}
	
				else
				{
					request.getRequestDispatcher("/WEB-INF/afficherEnchere.jsp").forward(request, response);
				}
			}
			else
			{
				request.getRequestDispatcher("/WEB-INF/afficherEnchere.jsp").forward(request, response);
			}		
		}
		catch (DaoException e) 
		{
			this.envoieErreur(e.getMessage(), request, response);
		}
    }

    private void envoieErreur(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("erreurDao", erreur);
        request.getRequestDispatcher("/WEB-INF/erreur/erreur.jsp").forward(request, response);
    }
}
