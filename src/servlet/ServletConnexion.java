package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.BeanUtilisateur;
import dao.DaoException;
import dao.DaoUtilisateur;

/**
 * Servlet utile à la connexion de l'utilisateur.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/connexion" ,loadOnStartup=1)
public class ServletConnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String identifiant = request.getParameter("identifiant");
		String motDePasse = request.getParameter("motDePasse");		

		boolean isAdministrateur = false;
		if(identifiant == null )identifiant="";
		if(motDePasse == null )motDePasse="";
		
		HttpSession session = request.getSession();
		request.setAttribute("identifiant", identifiant);
		request.setAttribute("motDePasse", motDePasse);
		//recuperation des variables session		

		session.setAttribute("isAdministrateur", isAdministrateur);
		
		if((boolean)session.getAttribute("isConnecte") == false)
		{
			request.getRequestDispatcher("/WEB-INF/connexion.jsp").forward( request,  response);
		}
		else
		{
			response.sendRedirect(request.getContextPath()+"/");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		BeanUtilisateur utilisateurLog = new BeanUtilisateur();
		DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
		
		String identifiant = request.getParameter( "identifiant" );
		String motDePasse = request.getParameter( "motDePasse" );	
		
		HttpSession session = request.getSession();

		try
		{			
			utilisateurLog = daoUtilisateur.connexionUtilisateur(identifiant,motDePasse);
			
			if( utilisateurLog != null )
			{
				// Si bonne connexion, affichage nouvelle page
				session.setAttribute( "isConnecte", true ); //récupération
				session.setAttribute("identifiant", identifiant);
				session.setAttribute( "pseudo", utilisateurLog.getPseudo() );
				session.setAttribute( "isAdministrateur", utilisateurLog.isAdministrateur() );
				session.setAttribute( "succes", "Vous êtes connecté");				
				
				response.sendRedirect(request.getContextPath()+"/");				
			}
			else
			{
				// Si mauvaise connexion, affichage du formulaire
				session.setAttribute("isConnecte", false);
				session.setAttribute("identifiant", "");
				session.setAttribute("motDePasse", "");
				session.setAttribute("erreur", "Login ou Password incorrect");
				
				request.getRequestDispatcher( "/WEB-INF/connexion.jsp").forward( request,  response);
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
