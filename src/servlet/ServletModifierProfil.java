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
 * Servlet servant au fonctionnement des modifications de profils.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/membre/modifierProfil", loadOnStartup = 1)
public class ServletModifierProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
		HttpSession session =(HttpSession) request.getSession();
		
		String pseudo = (String) session.getAttribute("pseudo");

		try
		{
			request.setAttribute("utilisateurActuel",daoUtilisateur.utilisateurParPseudo(pseudo));
			request.getRequestDispatcher("/WEB-INF/membre/modifierProfil.jsp").forward(request, response);
		}
		catch (DaoException e)
		{
			this.envoieErreur(e.getMessage(), request, response);
		}		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int noUtilisateur = Integer.parseInt (request.getParameter("noUtilisateur"));
		
		DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
		BeanUtilisateur utilisateurActuel = new BeanUtilisateur();

		try
		{
			utilisateurActuel = daoUtilisateur.utilisateurParNumero(noUtilisateur);		
		
			HttpSession session = request.getSession();
			
			/* Récupération des valeurs actuelles	*/	
			int credit = utilisateurActuel.getCredit();
			boolean administrateur = utilisateurActuel.isAdministrateur();
			String motDePasseActuel = utilisateurActuel.getMotDePasse();
			String emailActuel = utilisateurActuel.getEmail();
			String pseudoActuel = utilisateurActuel.getPseudo();
			
			String erreurMsg = "";
			String motDePasse = "";
			String btn = request.getParameter( "btn");
			boolean erreur = false;
			
			switch (btn)
			{
				case "Enregistrer" :			
			
					/* Récupération des valeurs saisies */
					String pseudo = request.getParameter("pseudo");
					String nom = request.getParameter("nom");
					String prenom = request.getParameter("prenom");
					String email = request.getParameter("email");
					String telephone = request.getParameter("telephone");
					String rue = request.getParameter("rue");
					String codePostal = request.getParameter("codePostal");
					String ville = request.getParameter("ville");
					String ancienMdp = request.getParameter("ancienMdp");
					String nouveauMdp = request.getParameter("nouveauMdp");
					String confirmationMdp = request.getParameter("confirmationMdp");
					
					/* test sur les champs vide */
					
					if(pseudo.equals("") || nom.equals("") || prenom.equals("") || email.equals("") || rue.equals("") 
							|| codePostal.equals("") || ville.equals("") )
					{					
						erreurMsg = erreurMsg + "Tous les champs ne sont pas renseignés<br/>";					
						erreur = true;
					}	
					
					/* test sur email présent dans la base de données ou identique au précédent */
					if((daoUtilisateur.isEmailExistant(email)) && !(email.equals(emailActuel))  )
					{
						erreurMsg = erreurMsg +  "Email existe déjà ou ne correspond pas à votre email actuel<br/>";
						erreur = true;
					}
					/* test sur pseudo présent dans la base de données ou identique au précédent */
					if((daoUtilisateur.isPseudoExistant(pseudo)) && !pseudo.equals(pseudoActuel)  )
					{
						erreurMsg = erreurMsg + "Pseudo existe déjà ou ne correspond pas à votre pseudo actuel<br/>";
						erreur = true;
					}				
	
					/* test sur ancien mot de passe si vide pas de modification, si renseigner control avec celui de la base et si ok control du nouveau mot de passe */
					if(!ancienMdp.equals(""))
					{
						if(ancienMdp.equals(motDePasseActuel))
						{							
							if(daoUtilisateur.isMotDePasseFormatOk(nouveauMdp) && nouveauMdp.equals(confirmationMdp))
							{
								motDePasse = nouveauMdp;							
							}
							else
							{
								erreurMsg = erreurMsg +  "Problème de saisie sur les mots de passe<br/>";
								erreur = true;
							}
						}	
						else
						{
							erreurMsg = erreurMsg +  "Le mot de passe actuel ne correspond pas<br/>";
							erreur = true;
						}	
					}
					else
					{
						motDePasse = motDePasseActuel;						
					}						
									
					if( erreur )
					{
						session.setAttribute("erreur", erreurMsg);
					}
					else
					{
						
						utilisateurActuel.setPseudo(pseudo);
						/* Au changement de pseudo, mise à jour de la variable pseudo au niveau session */
						session.setAttribute("pseudo", pseudo);
						utilisateurActuel.setNom(nom); 
						utilisateurActuel.setPrenom(prenom);
						utilisateurActuel.setEmail(email);								
						utilisateurActuel.setTelephone(telephone);
						utilisateurActuel.setRue(rue);
						utilisateurActuel.setCodePostal(codePostal);
						utilisateurActuel.setVille(ville);
						utilisateurActuel.setMotDePasse(motDePasse);	
						
						daoUtilisateur.modifierUtilisateur(utilisateurActuel);						
	
						session.setAttribute("succes", "Modification du profil réalisé avec succès");
						
					}	
					
					doGet(request,response);
					
				break;
				
				case "Supprimer" :						 
						daoUtilisateur.supprimerUtilisateur(utilisateurActuel);
						session.setAttribute("pseudo","");						
						response.sendRedirect(request.getContextPath()+"/deconnexion");					
				break;
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