package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.BeanArticleVendu;
import bean.BeanRetrait;
import bean.BeanUtilisateur;
import dao.DaoArticlesVendus;
import dao.DaoException;
import dao.DaoRetrait;
import dao.DaoUtilisateur;
import metier.SecuriteVente;

/**
 * Servlet servant au fonctionnement des modifications des articles.
 * @author DWWM-Equipe #1
 */
@WebServlet("/membre/modifierArticle")
public class ServletModifierArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		BeanArticleVendu article;
		BeanRetrait retrait;
		HttpSession session =  request.getSession();

		article = (BeanArticleVendu) session.getAttribute("article");

		try {
			retrait = DaoRetrait.obtenirRetrait(article.getNoArticle());
			request.setAttribute("retrait", retrait);
			request.getRequestDispatcher("/WEB-INF/membre/modifierArticle.jsp").forward(request, response);

		} catch (DaoException e) {

			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		String erreurArticle;
		String erreurRetrait;
		BeanArticleVendu articleActuel;

		articleActuel = (BeanArticleVendu) session.getAttribute("article");

		// article
		String nomArticle =  request.getParameter("nomArticle");
		String description = request.getParameter("description");

		// parsage des dates
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parseDebut = new Date();
		Date parseFin = new Date();

		try
		{
			try
			{
				parseDebut = format.parse(request.getParameter("dateDebutEncheres"));
				parseFin = format.parse(request.getParameter("dateFinEncheres"));
			}
			catch (ParseException e)
			{
				System.err.println("Parsage de la date �chou�.");
			}
			java.sql.Date dateDebutEncheres = new java.sql.Date(parseDebut.getTime());
			java.sql.Date dateFinEncheres = new java.sql.Date(parseFin.getTime());

			int prixInitial = Integer.valueOf( request.getParameter("prixInitial") );

			int noCategorie = Integer.valueOf( request.getParameter("noCategorie") );
			String photoUrl = request.getParameter("photo");

			// hydrate l'article
			BeanArticleVendu articleVendu = new BeanArticleVendu();
			articleVendu.setNoArticle(articleActuel.getNoArticle());
			articleVendu.setNomArticle(nomArticle);
			articleVendu.setDescription(description);
			articleVendu.setDateDebutEncheres(dateDebutEncheres);
			articleVendu.setDateFinEncheres(dateFinEncheres);
			articleVendu.setPrixInitial(prixInitial);
			articleVendu.setPrixVente(prixInitial);
			articleVendu.setNoUtilisateur(articleActuel.getNoUtilisateur());
			articleVendu.setUtilisateur(articleActuel.getUtilisateur());
			articleVendu.setNoCategorie(noCategorie);

			// verif de l'article

			erreurArticle = SecuriteVente.verifArticle(articleVendu);

			if (erreurArticle.isEmpty())
			{
				DaoArticlesVendus.modifierArticle(articleVendu);
				session.setAttribute("article", articleVendu);
			}
			else
			{
				session.setAttribute("erreur", erreurArticle);
			}

			// retrait
			String rue = request.getParameter("rue");
			String codePostal = request.getParameter("codePostal");
			String ville = request.getParameter("ville");

			BeanRetrait retrait = new BeanRetrait();

			retrait.setNoArticle( DaoArticlesVendus.numeroArticleParProprietes(articleVendu) );

			retrait.setRue(rue);
			retrait.setCodePostal(codePostal);
			retrait.setVille(ville);

			// vérif du retrait
			erreurRetrait = SecuriteVente.verifRetrait(retrait);
			if (erreurRetrait.isEmpty())
			{
				DaoRetrait.modifierRetrait(retrait);
				System.out.println(retrait);
				session.setAttribute("retrait", retrait);
				session.setAttribute("succes", "Modification de l'objet réussie");
			}

			if (!erreurArticle.isEmpty() || !erreurRetrait.isEmpty())
			{
				session.setAttribute("erreur", erreurArticle + ", " + erreurRetrait);
			}

			this.doGet(request, response);
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