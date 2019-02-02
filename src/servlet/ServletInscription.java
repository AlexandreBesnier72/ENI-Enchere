package servlet;

import bean.BeanUtilisateur;
import dao.DaoException;
import dao.DaoUtilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet permettant à l'utilisateur de pouvoir s'inscrire.
 * Redirection en cas de réussite à l'inscription.
 * Redirection vers la page d'erreur en cas d'une erreur avec la servlet ou avec un fichier.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns = "/inscription", loadOnStartup = 1)
public class ServletInscription extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
        BeanUtilisateur utilisateur = new BeanUtilisateur();

        String pseudo = request.getParameter("pseudo");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telepone = request.getParameter("telephone");
        String rue = request.getParameter("rue");
        String ville = request.getParameter("ville");
        String codePostal = request.getParameter("codePostal");
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmation");
        String erreurMsg = "";
        boolean erreur = false;
        
        try
        {
	        if(pseudo.equals("") || nom.equals("") || prenom.equals("") || email.equals("") || rue.equals("") 
					|| codePostal.equals("") || ville.equals("")  || motDePasse.equals("") || confirmation.equals(""))
	        {
	        	erreurMsg = "Certains champs n'ont pas été renseignés.";
	        	session.setAttribute("erreur", erreurMsg );
	        	erreur = true;
	        }
	
	        if (!motDePasse.equals(confirmation))
	        {
	        	erreurMsg = erreurMsg + "<br/> Les deux mots de passe ne sont pas identiques.";
	            session.setAttribute("erreur",erreurMsg );
	            erreur = true;
	        }
	
	        if (daoUtilisateur.isEmailExistant(email))
	        {
	            erreurMsg = erreurMsg + "<br/> Cet email est déjà utilisé.";
	            session.setAttribute("erreur",erreurMsg);
	            erreur = true;
	        }
	
	        if (daoUtilisateur.isPseudoExistant(pseudo))
	        {
	            erreurMsg = erreurMsg + "<br/> Ce pseudo existe déjà.";
	            session.setAttribute("erreur",erreurMsg);
	            erreur = true;
	        }
	
	        if (!daoUtilisateur.isMotDePasseFormatOk(motDePasse))
	        {
	        	erreurMsg = erreurMsg + "<br/>Le mot de passe n'est pas correct";
	            session.setAttribute("erreur", erreurMsg);
	            erreur = true;
	        }
	
	        if (!erreur)
	        {
	            utilisateur.setPseudo(pseudo);
	            utilisateur.setNom(nom);
	            utilisateur.setPrenom(prenom);
	            utilisateur.setEmail(email);
	            utilisateur.setTelephone(telepone);
	            utilisateur.setRue(rue);            
	            utilisateur.setVille(ville);
	            utilisateur.setCodePostal(codePostal);
	            utilisateur.setMotDePasse(motDePasse);
	
	            daoUtilisateur.ajouterUtilisateur(utilisateur);
	
	            session.setAttribute("succes", "Connexion réussie");
	            response.sendRedirect(request.getContextPath() + "/");
	        }
	        else
	        {
	            request.getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
	        		
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
