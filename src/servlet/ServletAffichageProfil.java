package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoException;
import dao.DaoUtilisateur;

/**
 * Servlet servant au fonctionnement de l'affichage du profil.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/affichageProfil" ,loadOnStartup=1)
public class ServletAffichageProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		/* récupération du pseudo cliqué */
		String pseudoReq = request.getParameter("pseudo");
		
	     DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	     /* récupération l'utilisateur du pseudo cliqué */
		try
		{
			request.setAttribute("utilisateur", daoUtilisateur.utilisateurParPseudo(pseudoReq));
		
		
			// on repousse pseudoRequete
			request.setAttribute("pseudo", pseudoReq);
			
			request.getRequestDispatcher( "/WEB-INF/affichageProfil.jsp").forward( request,  response);
		}
		catch (DaoException e)
		{
			this.envoieErreur(e.getMessage(), request, response);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect(request.getContextPath()+"/membre/modifierProfil");
	}

	private void envoieErreur(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("erreurDao", erreur);
		request.getRequestDispatcher("/WEB-INF/erreur/erreur.jsp").forward(request, response);
	}
}
