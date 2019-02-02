package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.*;
import dao.DaoArticlesVendus;
import dao.DaoCategorie;
import dao.DaoException;
import dao.DaoUtilisateur;

/**
 * Servlet servant au fonctionnement de la page index.
 * @author DWWM-Equipe #1
 */
@WebServlet(urlPatterns="/", loadOnStartup=1 )
public class ServletIndex extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Initialisation de la session
		HttpSession session = request.getSession();

		List<BeanCategorie> listeCategories;
		Map<String, List<BeanArticleVendu>> listeArticles;
		BeanFiltreRecherche filtreRecherche = null;
		int noUtilisateur;

		try
		{
			if (!session.getAttribute("pseudo").equals(""))
			{
				noUtilisateur = DaoUtilisateur.utilisateurParPseudo( (String) session.getAttribute("pseudo") ).getNoUtilisateur();
			}
			else
			{
				noUtilisateur = 0;
			}

			// catégories
			listeCategories = DaoCategorie.listerCategories();

			if (session.getAttribute("filtreRecherche") != null)
			{
				filtreRecherche = (BeanFiltreRecherche) session.getAttribute("filtreRecherche");
			}
			request.getSession().setAttribute("categories", listeCategories);

			// articles
			listeArticles = DaoArticlesVendus.rechercher(filtreRecherche, noUtilisateur);

			// hydratation des listes d'article
			if (listeArticles.get("encheresO") != null)
			{
				request.setAttribute("encheresO", listeArticles.get("encheresO"));
			}

			if (listeArticles.get("mesEncheresEC") != null)
			{
				request.setAttribute("mesEncheresEC", listeArticles.get("mesEncheresEC"));
			}

			if (listeArticles.get("mesEncheresR") != null)
			{
				request.setAttribute("mesEncheresR", listeArticles.get("mesEncheresR"));
			}
			
			if(listeArticles.get("mesVentes") != null)
			{
				request.setAttribute("mesVentes", listeArticles.get("mesVentes"));
			}

			if (listeArticles.get("mesVentesEC") != null)
			{
				request.setAttribute("mesVentesEC", listeArticles.get("mesVentesEC"));
			}

			if (listeArticles.get("mesVentesND") != null)
			{
				request.setAttribute("mesVentesND", listeArticles.get("mesVentesND"));
			}

			if (listeArticles.get("mesVentesT") != null)
			{
				request.setAttribute("mesVentesT", listeArticles.get("mesVentesT"));
			}

			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
		}
		catch (DaoException e)
		{
			this.envoieErreur(e.getMessage(), request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();

		// hydratation du filtre de recherche
		BeanFiltreRecherche filtreRecherche = new BeanFiltreRecherche();

		filtreRecherche.setNomArticle(request.getParameter("recherche"));

		if (request.getParameter("categorie").equals("Toutes"))
		{
			filtreRecherche.setNoCategorie(-1);
		}
		else
		{
			filtreRecherche.setNoCategorie( Integer.valueOf( request.getParameter("categorie") ) );
		}

		if (request.getParameter("radioMenu") != null)
		{
			if (request.getParameter("radioMenu").equals("achats"))
			{
				// hydratation de l'achat
				BeanAchats achats = new BeanAchats();

				if (request.getParameter("encheresOuvertes") != null)
				{
					achats.setEncheresOuvertes( true );
				}
				else
				{
					achats.setEncheresOuvertes( false );
				}

				if (request.getParameter("encheresEnCours") != null)
				{
					achats.setEncheresEnCours( true );
				}
				else
				{
					achats.setEncheresEnCours( false );
				}

				if (request.getParameter("encheresRemportees") != null)
				{
					achats.setEncheresRemportees( true );
				}
				else
				{
					achats.setEncheresRemportees( false );
				}

				filtreRecherche.setAchats(achats);
			}
			else if(request.getParameter("radioMenu").equals("ventes"))
			{
				// hydratation de la vente
				BeanVentes ventes = new BeanVentes();

				if (request.getParameter("ventesEnCours") != null)
				{
					ventes.setVentesEnCours( true );
				}
				else
				{
					ventes.setVentesEnCours( false );
				}

				if (request.getParameter("ventesNonDebutees")!= null)
				{
					ventes.setVentesNonDebutees( true );
				}
				else
				{
					ventes.setVentesNonDebutees( false );
				}

				if (request.getParameter("ventesTerminees") != null)
				{
					ventes.setVentesTerminees( true );
				}
				else
				{
					ventes.setVentesTerminees( false );
				}

				filtreRecherche.setVentes(ventes);
			}
		}

		// envoie du filtre à la session
		session.setAttribute("filtreRecherche", filtreRecherche);

		doGet(request, response);
	}
	
	private void envoieErreur(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("erreurDao", erreur);
		request.getRequestDispatcher("/WEB-INF/erreur/erreur.jsp").forward(request, response);
	}
}
