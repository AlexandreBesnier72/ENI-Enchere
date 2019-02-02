package servlet;

import bean.BeanArticleVendu;
import bean.BeanCategorie;
import bean.BeanRetrait;
import bean.BeanUtilisateur;
import dao.*;
import metier.SecuriteVente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet servant à l'ajout d'enchère et à la redirection vers la page d'erreur si une erreur est rencontrée.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns = "/membre/ajouterEnchere", loadOnStartup = 1)
public class ServletAjoutArticle extends HttpServlet
{
	private static final long serialVersionUID = 8369475205568574772L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		HttpSession session = request.getSession();
		
        DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
        DaoArticlesVendus daoArticlesVendus = new DaoArticlesVendus();
        DaoRetrait daoRetrait = new DaoRetrait();
        BeanUtilisateur utilisateur = new BeanUtilisateur();
        BeanArticleVendu articleVendu = new BeanArticleVendu();
        BeanRetrait retrait = new BeanRetrait();
        int noUtilisateur = 0;
        
        // Dates
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parseDebut = new Date();
        Date parseFin = new Date();
        
    	// erreurs
        String erreurArticle = "";
        String erreurRetrait = "";

        // article
        String nomArticle =  request.getParameter("nomArticle");
        String description = request.getParameter("description");
        int noCategorie = Integer.valueOf( request.getParameter("noCategorie") );
        int prixInitial = Integer.valueOf( request.getParameter("prixInitial") );
	    int prixVente = Integer.valueOf( request.getParameter("prixInitial") );
	    
        // retrait
        String rue = request.getParameter("rue");
        String codePostal = request.getParameter("codePostal");
        String ville = request.getParameter("ville");
        
        String photoUrl = request.getParameter("photo");
        
        try
        {        
	        // parsage des dates
	        try
	        {
	            parseDebut = format.parse(request.getParameter("dateDebutEncheres"));
	            parseFin = format.parse(request.getParameter("dateFinEncheres"));
	        }
	        catch (ParseException e)
	        {
	            System.err.println("Parsage de la date échoué.");
	        }
	        
	        java.sql.Date dateDebutEncheres = new java.sql.Date(parseDebut.getTime());
	        java.sql.Date dateFinEncheres = new java.sql.Date(parseFin.getTime());
	
	        utilisateur = daoUtilisateur.utilisateurParPseudo( (String) session.getAttribute("pseudo") );	
	
	        noUtilisateur = utilisateur.getNoUtilisateur();
	
	        // hydrate l'article
	        articleVendu.setNomArticle(nomArticle);
	        articleVendu.setDescription(description);
	        articleVendu.setDateDebutEncheres(dateDebutEncheres);
	        articleVendu.setDateFinEncheres(dateFinEncheres);
	        articleVendu.setPrixInitial(prixInitial);
	        articleVendu.setPrixVente(prixVente);
	        articleVendu.setNoUtilisateur(noUtilisateur);
	        articleVendu.setUtilisateur(utilisateur);
	        articleVendu.setNoCategorie(noCategorie);
	
	        // verification de l'article
	        erreurArticle = SecuriteVente.verifArticle(articleVendu);
	        if (erreurArticle.isEmpty())
	        {
	        	daoArticlesVendus.ajouterArticle(articleVendu);
	        	int noArticleVendu = daoArticlesVendus.numeroArticleParProprietes(articleVendu);
	        	
	        	retrait.setNoArticle( noArticleVendu );
		        retrait.setRue(rue);
		        retrait.setCodePostal(codePostal);
		        retrait.setVille(ville);
	
		        // vérification du retrait
	        	erreurRetrait = SecuriteVente.verifRetrait(retrait);
		        if (erreurRetrait.isEmpty())
		        {					
		        	daoRetrait.ajouterRetrait(retrait);
		        }   
		        else
		        {
		        	daoArticlesVendus.supprimerArticle(noArticleVendu);
		        }
	        }
	        
	        
	
	        // Redirige la requête selon les statuts d'erreur
	        if (!erreurArticle.isEmpty() || !erreurRetrait.isEmpty())
	        {
	            session.setAttribute("erreur", erreurArticle + "\n" + erreurRetrait);
	            this.doGet(request, response);
	        }
	        else
	        {
	            session.setAttribute("succes", "ajout de l'objet réussi");
	            response.sendRedirect(request.getContextPath() + "/");
	        }
        }
        catch(DaoException e)
        {
        	envoieErreur(e.getMessage(), request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        DaoCategorie daoCategorie = new DaoCategorie();
        DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
        BeanUtilisateur utilisateur = new BeanUtilisateur();
        BeanRetrait retrait = new BeanRetrait();
        List<BeanCategorie> listeCategories = new ArrayList<>();

        try
        {
            utilisateur = daoUtilisateur.utilisateurParPseudo( (String) session.getAttribute("pseudo") );
            listeCategories = daoCategorie.listerCategories();
            
            if(utilisateur != null)
            {
		        // retrait à envoyer à la vue
		        retrait.setRue(utilisateur.getRue());
		        retrait.setCodePostal(utilisateur.getCodePostal());
		        retrait.setVille(utilisateur.getVille());
            }
            else
            {
            	retrait.setRue("");
            	retrait.setCodePostal("");
            	retrait.setVille("");
            }	
	        // actualisation des variables que récupère la vue, et redirection
	        request.setAttribute("retrait", retrait);
            session.setAttribute("categories", listeCategories);
            request.getRequestDispatcher("/WEB-INF/membre/ajouterEnchere.jsp").forward(request, response);
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
