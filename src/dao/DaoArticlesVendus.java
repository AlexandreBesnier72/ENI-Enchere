package dao;

import bean.BeanArticleVendu;
import bean.BeanCategorie;
import bean.BeanFiltreRecherche;
import bean.BeanUtilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.AccesBase;

/**
 * Classe permettant d'ajouter, modifier, rechercher et supprimer des articles
 * dans la base de données.
 * @author DWWM-Equipe #1
 */
public class DaoArticlesVendus 
{
	// Recherche des articles en vente (ciblage par vendeur)
	private static final String RECHERCHER_VENTES = "SELECT a.no_article,nom_article,a.no_categorie,libelle,description,"
			+ "date_debut_encheres,date_fin_encheres,prix_initial,prix_vente,"
			+ "a.no_utilisateur,pseudo,nom,prenom,email,telephone,rue,code_postal,ville,"
			+ "mot_de_passe,credit,administrateur "
			+ "FROM ARTICLES_VENDUS a "
			+ "INNER JOIN UTILISATEURS u ON u.no_utilisateur = a.no_utilisateur "
			+ "INNER JOIN CATEGORIES c ON c.no_categorie = a.no_categorie "
			+ "WHERE a.no_utilisateur=? ";
	
	// Recherche des enchères effectuées (dernière enchère sur chaque article)
	private static final String RECHERCHER_ACHATS = "SELECT a.no_article,nom_article,a.no_categorie,"
			+ "c.libelle,description,"
			+ "date_debut_encheres,date_fin_encheres,prix_initial,prix_vente,"
			+ "a.no_utilisateur,pseudo,nom,prenom,email,telephone,rue,code_postal,ville,"
			+ "mot_de_passe,credit,administrateur "
			+ "FROM ARTICLES_VENDUS a "
			+ "INNER JOIN UTILISATEURS u ON u.no_utilisateur = a.no_utilisateur "
			+ "INNER JOIN ENCHERES e ON e.no_article = a.no_article "
			+ "INNER JOIN CATEGORIES c ON c.no_categorie = a.no_categorie "
			+ "WHERE 1=1 ";

	// Toutes les enchères en cours
	private static final String RECHERCHER_ENCHERES_O = "SELECT a.no_article,nom_article,a.no_categorie,libelle,description,"
			+ "date_debut_encheres,date_fin_encheres,prix_initial,prix_vente,"
			+ "a.no_utilisateur,pseudo,nom,prenom,email,telephone,rue,code_postal,ville,"
			+ "mot_de_passe,credit,administrateur "
			+ "FROM ARTICLES_VENDUS a "
			+ "INNER JOIN UTILISATEURS u ON u.no_utilisateur = a.no_utilisateur "
			+ "INNER JOIN CATEGORIES c ON c.no_categorie = a.no_categorie "
			+ "WHERE 1=1 "
			+ "AND GETDATE() "
			+ "BETWEEN date_debut_encheres AND date_fin_encheres ";
	
	// Enchères en cours d'un utilisateur
	private static final String RECHERCHER_MES_ENCHERES_EC = RECHERCHER_ACHATS 
			+ "AND CONVERT(DATE,(SELECT MAX(date_enchere) "
			+ "					 FROM ENCHERES AS e1 WHERE e1.no_article=e.no_article)) "
			+ "BETWEEN date_debut_encheres AND date_fin_encheres "
			+ "AND e.no_utilisateur=? ";
	
	// Enchères remportées par un utilisateur
	private static final String RECHERCHER_MES_ENCHERES_R = RECHERCHER_ACHATS 
			+ "AND date_enchere=(SELECT MAX(date_enchere) "
			+ "					 FROM ENCHERES AS e1 WHERE e1.no_article=e.no_article) "
			+ "AND CONVERT(DATE, GETDATE()) > date_fin_encheres "
			+ "AND e.no_utilisateur=? ";
	
	// Ventes en cours d'un utilisateur
	private static final String RECHERCHER_MES_VENTES_EC = RECHERCHER_VENTES 
			+ "AND CONVERT(DATE, GETDATE()) BETWEEN date_debut_encheres AND date_fin_encheres ";
	
	// Ventes non débutées d'un utilisateur
	private static final String RECHERCHER_MES_VENTES_ND = RECHERCHER_VENTES 
			+ "AND date_debut_encheres > GETDATE() ";
	
	// Ventes terminées d'un utilisateur
	private static final String RECHERCHER_MES_VENTES_T = RECHERCHER_VENTES 
			+ "AND GETDATE() > date_fin_encheres ";
	
	private static final String AFFINAGE_NOM = "AND nom_article LIKE ? ";
	
	private static final String AFFINAGE_CATEGORIE = "AND a.no_categorie=? ";
	
	private static final String AJOUTER_ARTICLE = "INSERT INTO ARTICLES_VENDUS (nom_article,"
			+ "description,date_debut_encheres,date_fin_encheres,"
			+ "prix_initial,prix_vente,no_utilisateur,no_categorie) "
			+ "VALUES (?,?,?,?,?,?,?,?);";
	
	private static final String RECHERCHER_NUM_ARTICLE = "SELECT no_article FROM ARTICLES_VENDUS "
			+ "WHERE nom_article=? AND description=? AND date_debut_encheres=? "
			+ "AND date_fin_encheres=? AND prix_initial=? AND prix_vente=? "
			+ "AND no_utilisateur=? AND no_categorie=?;";
	
	private static final String ARTICLE_PAR_NUMERO = "SELECT no_article,nom_article,a.no_categorie,"
			+ "libelle,description,date_debut_encheres,date_fin_encheres,"
			+ "prix_initial,prix_vente,u.no_utilisateur,pseudo,nom,prenom,email,telephone,rue,"
			+ "code_postal,ville,mot_de_passe,credit,administrateur "
			+ "FROM ARTICLES_VENDUS a "
			+ "INNER JOIN UTILISATEURS u ON u.no_utilisateur = a.no_utilisateur "
			+ "INNER JOIN CATEGORIES c ON c.no_categorie = a.no_categorie "
			+ "WHERE no_article=?;";
	
	private static final String MODIFIER_ARTICLE ="UPDATE ARTICLES_VENDUS SET nom_article=?,"
			+ "description=?,date_debut_encheres=?,date_fin_encheres=?,prix_initial=?,prix_vente=?,"
			+ "no_categorie=? WHERE no_article=?;";
	
	private static final String MODIFIER_PRIX_ARTICLE = "UPDATE ARTICLES_VENDUS SET prix_vente=? "
			+ "WHERE no_article=?;";
	
	private static final String SUPPRIMER_ARTICLE = "DELETE FROM ARTICLES_VENDUS WHERE no_article=?;";
	
	/**
	 *  Listing de tous les articles en enchères selon le filtre renseigné par l'utilisateur.
	 * @param filtre Contexte de recherche défini par l'utilisateur.
	 * @param noUtilisateur Numéro de l'utilisateur.
	 * @return Retourne le résultat des recherches filtrées sous forme de Map pour les catégoriser sur la page de recherche.
	 * @throws DaoException Propage l'exception.
	 */
	public static HashMap<String, List<BeanArticleVendu>> rechercher(BeanFiltreRecherche filtre, int noUtilisateur) throws DaoException
	{
		List<BeanArticleVendu> encheresO= new ArrayList<>();
		List<BeanArticleVendu> mesEncheresEC= new ArrayList<>();
		List<BeanArticleVendu> mesEncheresR= new ArrayList<>();
		List<BeanArticleVendu> mesVentes = new ArrayList<>();
		List<BeanArticleVendu> mesVentesEC= new ArrayList<>();
		List<BeanArticleVendu> mesVentesND= new ArrayList<>();
		List<BeanArticleVendu> mesVentesT= new ArrayList<>();

		// Initialisation de la map de résultats avec NULL pour chaque clef
		HashMap<String, List<BeanArticleVendu>> mapRecherche = new HashMap<String, List<BeanArticleVendu>>();
		mapRecherche.put("encheresO", null);
		mapRecherche.put("mesEncheresEC", null);
		mapRecherche.put("mesEncheresR", null);
		mapRecherche.put("mesVentes", null);
		mapRecherche.put("mesVentesEC", null);
		mapRecherche.put("mesVentesND", null);
		mapRecherche.put("mesVentesT", null);
		
		// Requete pour la recherche qui sera déterminée par tous les éléments du filtre
		String sqlRecherche ="";
		
		// Chaîne de contrôle pour déterminer les PreparedStatements
		String statut = "";

		try(Connection connexion = AccesBase.dbConnexion())
		{
			if(filtre == null) 
			{	
				sqlRecherche = RECHERCHER_ENCHERES_O;
				statut = "EO";
				
				// Récupération de la liste de résultats, et ajout à la map finale
				encheresO.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));

				if(!encheresO.isEmpty())
				{
					mapRecherche.put("encheresO", encheresO);
				}
			}
			else
			{	
				// Si le nom d'article recherché n'est pas vide, on rajoute un "%" de chaque coté 
				// de la chaîne pour le LIKE dans la requete SQL
				if (!filtre.getNomArticle().isEmpty())
				{
					filtre.setNomArticle( "%" + filtre.getNomArticle() + "%" );
				}
	
				// Recherche pour le mode connecté (bouton radio "achats" OU "ventes" actif)			
				// On vérifie les critères pour les achats
				if(filtre.getAchats() != null)
				{
					// Si la case "Enchères ouvertes" est cochée
					if(filtre.getAchats().isEncheresOuvertes())
					{
						sqlRecherche = RECHERCHER_ENCHERES_O;
						statut = "EO";
						encheresO.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));

						if(!encheresO.isEmpty())
						{
							mapRecherche.put("encheresO", encheresO);
						}
					}
					
					// Si la case "Mes enchères en cours" est cochée
					if(filtre.getAchats().isEncheresEnCours())
					{
						sqlRecherche = RECHERCHER_MES_ENCHERES_EC;
						statut = "EC";
						mesEncheresEC.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));

						if(!mesEncheresEC.isEmpty())
						{
							mapRecherche.put("mesEncheresEC", mesEncheresEC);
						}
					}
					
					// Si la case "Mes enchères remportées" est cochée
					if(filtre.getAchats().isEncheresRemportees())
					{
						sqlRecherche = RECHERCHER_MES_ENCHERES_R;
						statut = "ER";
						mesEncheresR.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));
						
						if(!mesEncheresR.isEmpty())
						{
							mapRecherche.put("mesEncheresR", mesEncheresR);
						}
					}

					// Si aucune case des achats n'est cochée (toutes enchères)
					if (!filtre.getAchats().isEncheresOuvertes() &&
						!filtre.getAchats().isEncheresEnCours() &&
						!filtre.getAchats().isEncheresRemportees())
					{
						sqlRecherche = RECHERCHER_ENCHERES_O;
						statut = "EO";
						encheresO.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));

						if(!encheresO.isEmpty())
						{
							mapRecherche.put("encheresO", encheresO);
						}
					}
				}
				else if(filtre.getVentes() != null)
				{	
					// Si la case "Mes ventes en cours" est cochée
					if(filtre.getVentes().isVentesEnCours())
					{
						sqlRecherche = RECHERCHER_MES_VENTES_EC;
						statut = "VC";
						mesVentesEC.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));
						
						if(!mesVentesEC.isEmpty())
						{
							mapRecherche.put("mesVentesEC", mesVentesEC);
						}
					}
					
					// Si la case "Mes ventes terminées" est cochée
					if(filtre.getVentes().isVentesTerminees())
					{
						sqlRecherche = RECHERCHER_MES_VENTES_T;
						statut = "VT";
						mesVentesT.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));
						
						if(!mesVentesT.isEmpty())
						{
							mapRecherche.put("mesVentesT", mesVentesT);
						}
					}
					
					// Si la case "Mes ventes non débutées" est cochée
					if(filtre.getVentes().isVentesNonDebutees())
					{
						sqlRecherche = RECHERCHER_MES_VENTES_ND;
						statut = "ND";
						mesVentesND.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));
						
						if(!mesVentesND.isEmpty())
						{
							mapRecherche.put("mesVentesND", mesVentesND);
						}
						System.out.println(mapRecherche.get("mesVentesND"));
					}

					// Si aucune case des ventes n'est cochée (toutes ventes de l'utilisateur)
					if (!filtre.getVentes().isVentesEnCours() &&
						!filtre.getVentes().isVentesTerminees() &&
						!filtre.getVentes().isVentesNonDebutees())
					{
						sqlRecherche = RECHERCHER_VENTES;
						statut = "MV";
						mesVentes.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));

						if(!mesVentes.isEmpty())
						{
							mapRecherche.put("mesVentes", mesVentes);
						}
					}
				}
				// Si les boutons radios ne sont pas actifs, l'utilisateur est déconnecté
				// donc la recherche porte sur toutes les enchères en cours par défaut
				else
				{
					sqlRecherche = RECHERCHER_ENCHERES_O;
					statut = "EO";
					encheresO.addAll(affinageRecherche(filtre, noUtilisateur, sqlRecherche, statut, connexion));
					
					if(!encheresO.isEmpty())
					{
						mapRecherche.put("encheresO", encheresO);
					}
				}
			}
			// On renvoie la map de résultats pour l'affichage sur la page d'accueil
			return mapRecherche;
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}
	
	/**
	 *  Mise en vente d'un nouvel article.
	 * @param nouvelArticle Le nouvel article que l'on va rajouter.
	 * @throws DaoException Propagation de l'exception.
	 */
	public static void ajouterArticle(BeanArticleVendu nouvelArticle) throws DaoException
	{		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(AJOUTER_ARTICLE))
			{
				ps.setString(1, nouvelArticle.getNomArticle());
				ps.setString(2, nouvelArticle.getDescription());
				ps.setDate(3, nouvelArticle.getDateDebutEncheres());
				ps.setDate(4, nouvelArticle.getDateFinEncheres());
				ps.setInt(5, nouvelArticle.getPrixInitial());
				ps.setInt(6, nouvelArticle.getPrixVente());
				ps.setInt(7, nouvelArticle.getNoUtilisateur());
				ps.setInt(8, nouvelArticle.getNoCategorie());
				
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de l'ajout d'un article : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}	
	
	/**
	 *  Recherche du numéro d'un article en renseignant ses autres propriétés.
	 * @param articleRecherche L'article recherché.
	 * @return Retourne le numéro de l'article recherché.
	 * @throws DaoException Propage l'exception.
	 */
	public static int numeroArticleParProprietes(BeanArticleVendu articleRecherche) throws DaoException
	{
		int noArticleTrouve = 0;
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(RECHERCHER_NUM_ARTICLE))
			{
				ps.setString(1, articleRecherche.getNomArticle());
				ps.setString(2, articleRecherche.getDescription());
				ps.setDate(3, articleRecherche.getDateDebutEncheres());
				ps.setDate(4, articleRecherche.getDateFinEncheres());
				ps.setInt(5, articleRecherche.getPrixInitial());
				ps.setInt(6, articleRecherche.getPrixVente());
				ps.setInt(7, articleRecherche.getNoUtilisateur());
				ps.setInt(8, articleRecherche.getNoCategorie());
				
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next())
					{
						noArticleTrouve = rs.getInt("no_article");
					}
					else
					{
						noArticleTrouve = -1;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la recherche d'un article par propriétés : "+e.getMessage());
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : "+e);
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
		return noArticleTrouve;
	}
	
	/**
	 *  Recherche d'un article par son numéro unique
	 * @param no_article Le numéro de l'article recherché.
	 * @return Retourne l'article recherché par son numéro.
	 * @throws DaoException Propagation de l'exception.
	 */
	public static BeanArticleVendu articleParNumero(int no_article) throws DaoException
	{
		BeanArticleVendu articleTrouve = new BeanArticleVendu();
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(ARTICLE_PAR_NUMERO))
			{
				ps.setInt(1, no_article);
				try(ResultSet rs = ps.executeQuery())
				{
					// Si la requete renvoie un résultat, cela signifie que l'article recherché existe, on va donc 
					// hydrater toutes ses propriétés (intrinsèques, objet BeanUtilisateur et objet BeanCatégorie)
					if(rs.next())
					{
						mappingArticle(articleTrouve, rs);
					}
					else
					{
						articleTrouve = null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la recherche de l'article par numéro : "+ e.getMessage());
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : "+e);
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : " + e);
		}
		return articleTrouve;
	}	
	
	/**
	 * Modification d'un article.
	 * @param modifArticle L'article modifié.
	 * @throws DaoException Ereeur gérée par la DaoException lors de sa levée.
	 */
	public static void modifierArticle(BeanArticleVendu modifArticle) throws DaoException
	{		
		try (Connection connexion = AccesBase.dbConnexion()) 
		{
			try(PreparedStatement ps = connexion.prepareStatement(MODIFIER_ARTICLE))
			{		
				ps.setString(1, modifArticle.getNomArticle());
				ps.setString(2, modifArticle.getDescription());
				ps.setDate(3, modifArticle.getDateDebutEncheres());
				ps.setDate(4, modifArticle.getDateFinEncheres());
				ps.setInt(5, modifArticle.getPrixInitial());
				ps.setInt(6, modifArticle.getPrixVente());
				ps.setInt(7, modifArticle.getNoCategorie());
				ps.setInt(8, modifArticle.getNoArticle());
				
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de l'ajout d'un article : " + e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		} 
	}
	
	/**
	 *  Modification du prix de vente d'un article (suite à une enchère).
	 * @param noArticle Le numéro de l'article que l'on veut modifier.
	 * @param nouveauPrix Le nouveau prix suite à une enchère.
	 * @throws DaoException Propage l'exception.
	 */
	public static void modifierPrixArticle(int noArticle,int nouveauPrix) throws DaoException
	{		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(MODIFIER_PRIX_ARTICLE))
			{
				ps.setInt(1, nouveauPrix);
				ps.setInt(2, noArticle);
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de la modification du prix de l'article : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : " + e);
		}
	}
	
	/**
	 * Affinage d'une recherche selon le nom d'article et la catégorie, et réalisation de cette recherche.
	 * @param filtre : Le filtre de recherche déterminé par l'utilisateur.
	 * @param noUtilisateur : Le numéro d'utilisateur associé à la session.
	 * @param sqlRecherche : La requête de recherche SQL complétée par le filtre et l'affinage.
	 * @param statut : String qui détermine si la recherche doit être globale ou ciblée sur l'utilisateur.
	 * @param connexion : La connexion à la base de données depuis le pool de connexions.
	 * @return Renvoie la liste des résultats trouvés pour la requête filtrée.
	 * @throws DaoException : Propagation si une exception est soulevée par les PreparedStatements ou les ResultSets.
	 */
	private static List<BeanArticleVendu> affinageRecherche(BeanFiltreRecherche filtre, int noUtilisateur,
			String sqlRecherche, String statut, Connection connexion) throws DaoException
	{
		// Création d'une liste pour les résultats de requête
		List<BeanArticleVendu> listeResultats = new ArrayList<>();
		
		// Chaîne de contrôle pour déterminer les PreparedStatements
		String statutAffinage = "";

		// si le filtre est null
		if (filtre == null)
		{
			statut = "EO";
			statutAffinage = "R";
		}
		// Critères de nom et de catégorie supplémentaires
		// Si l'utilisateur ne précise pas de nom d'article à
		// rechercher, la recherche reste générale puis on teste la catégorie
		else if (filtre.getNomArticle() == null || filtre.getNomArticle().isEmpty())
		{
			// Le filtre envoie le numéro de catégorie -1 si l'utilisateur
			// n'en a pas précisé pour la recherche
			if (filtre.getNoCategorie() == -1)
			{
				statutAffinage = "R";
			} else
			{
				sqlRecherche += AFFINAGE_CATEGORIE;
				statutAffinage += "C";
			}
		}
		// Si l'utilisateur a entré un nom d'article à chercher, on affine la recherche, puis on teste
		// la catégorie
		else
		{
			if (filtre.getNoCategorie() == -1)
			{
				sqlRecherche += AFFINAGE_NOM;
				statutAffinage += "N";
			} else
			{
				sqlRecherche += AFFINAGE_NOM + AFFINAGE_CATEGORIE;
				statutAffinage += "NC";
			}
		}
		sqlRecherche += ";";

		try(PreparedStatement ps = connexion.prepareStatement(sqlRecherche))
		{
			// Si la recherche porte sur toutes les enchères ouvertes, les seuls paramètres à 
			// préciser sont le nom de l'article et/ou la catégorie
			if(statut.equals("EO"))
			{
				switch(statutAffinage)
				{
					case "R":
						break;
					case "C":
						ps.setInt(1, filtre.getNoCategorie());
						break;
					case "N":
						ps.setString(1, filtre.getNomArticle());
						break;
					case "NC":
						ps.setString(1, filtre.getNomArticle());
						ps.setInt(2, filtre.getNoCategorie());
						break;
					default :
						System.out.println("Erreur d'affinage");
				}
			}
			// Sinon, il faut cibler l'utilisateur concerné par la recherche, puis
			// préciser les critères de nom et de catégorie
			else
			{
				ps.setInt(1, noUtilisateur);
				
				switch(statutAffinage)
				{
					case "R":
						break;
					case "C":
						ps.setInt(2, filtre.getNoCategorie());
						break;
					case "N":
						ps.setString(2, filtre.getNomArticle());
						break;
					case "NC":
						ps.setString(2, filtre.getNomArticle());
						ps.setInt(3, filtre.getNoCategorie());
						break;
					default :
						System.out.println("Erreur d'affinage");
				}
			}

			try(ResultSet rs = ps.executeQuery())
			{				
				while(rs.next())
				{
					// On crée un article courant à chaque fois que la requête renvoie un résultat
					BeanArticleVendu articleCourant = new BeanArticleVendu();					
					mappingArticle(articleCourant, rs);
					listeResultats.add(articleCourant);
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de la recherche des articles : "+e.getMessage());
			}
		}	
		catch(SQLException e)
		{
			throw new DaoException("Erreur de PreparedStatement lors de la recherche : "+e);
		}
		return listeResultats;
	}
	
	
	public static void supprimerArticle(int noArticle) throws DaoException
	{
		String sqlSuppression = SUPPRIMER_ARTICLE;
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(sqlSuppression))
			{
				ps.setInt(1, noArticle);
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement lors de la suppression d'article : "+e);
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		} 
	}
	
	/**
	 *  Hydratation d'un nouvel objet de type BeanArticleVendu
	 * @param articleTrouve L'article recherché.
	 * @param rs Variable par laquelle on récupère les attributs de l'article.
	 * @return L'article hydraté.
	 * @throws SQLException Exception levée lors d'une erreur d'accès à la base de données 
	 * ou toute autre erreur relative à la base de données.
	 */
	private static void mappingArticle(BeanArticleVendu articleTrouve, ResultSet rs) throws SQLException
	{
		articleTrouve.setNoArticle(rs.getInt("no_article"));
		articleTrouve.setNomArticle(rs.getString("nom_article"));
		articleTrouve.setDescription(rs.getString("description"));
		articleTrouve.setDateDebutEncheres(rs.getDate("date_debut_encheres"));
		articleTrouve.setDateFinEncheres(rs.getDate("date_fin_encheres"));
		articleTrouve.setPrixInitial(rs.getInt("prix_initial"));
		articleTrouve.setPrixVente(rs.getInt("prix_vente"));
		articleTrouve.setNoUtilisateur(rs.getInt("no_utilisateur"));
		articleTrouve.setNoCategorie(rs.getInt("no_categorie"));

		articleTrouve.setUtilisateur( new BeanUtilisateur() );
		
		articleTrouve.getUtilisateur().setNoUtilisateur(rs.getInt("no_utilisateur"));
		articleTrouve.getUtilisateur().setPseudo(rs.getString("pseudo"));
		articleTrouve.getUtilisateur().setNom(rs.getString("nom"));
		articleTrouve.getUtilisateur().setPrenom(rs.getString("prenom"));
		articleTrouve.getUtilisateur().setEmail(rs.getString("email"));
		if(rs.getString("telephone").isEmpty() || rs.getString("telephone") == null)
		{
			articleTrouve.getUtilisateur().setTelephone(null);
		}
		else
		{
			articleTrouve.getUtilisateur().setTelephone(rs.getString("telephone"));
		}
		articleTrouve.getUtilisateur().setRue(rs.getString("rue"));
		articleTrouve.getUtilisateur().setCodePostal(rs.getString("code_postal"));
		articleTrouve.getUtilisateur().setVille(rs.getString("ville"));
		articleTrouve.getUtilisateur().setMotDePasse(rs.getString("mot_de_passe"));
		articleTrouve.getUtilisateur().setCredit(rs.getInt("credit"));
		articleTrouve.getUtilisateur().setAdministrateur(rs.getInt("administrateur"));

		articleTrouve.setCategorie( new BeanCategorie() );
		
		articleTrouve.getCategorie().setNoCategorie(rs.getInt("no_categorie"));
		articleTrouve.getCategorie().setLibelle(rs.getString("libelle"));
	}
}
