package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.BeanEnchere;
import helper.AccesBase;

/**
 * Classe permettant de récupérer les catégories à partir de la base de données.
 * @author DWWM-Equipe #1
 */
public class DaoEncheres 
{
	private static final String LISTER_TOUT = "SELECT * FROM ENCHERES;";
	private static final String EXISTENCE_ENCHERE = "SELECT * FROM ENCHERES WHERE no_article=? "
											 +"AND no_utilisateur=?;";
	private static final String DERNIERE_ENCHERE = "SELECT * FROM ENCHERES e "
											 +"WHERE date_enchere=(SELECT MAX(date_enchere) "
											 +"FROM ENCHERES e1 WHERE e1.no_article=e.no_article) "
											 +"AND e.no_article=?;";

	/**
	 * Méthode pour ajouter ou mettre à jour une ench�re
	 * @param nouvelleEnchere L'enchère ciblée par la méthode.
	 * @throws DaoException Propage l'exception.
	 */
	public void nouvelleEnchere(BeanEnchere nouvelleEnchere) throws DaoException
	{	
		String ajoutEnchere = "";
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(EXISTENCE_ENCHERE))
			{
				ps.setInt(1, nouvelleEnchere.getNoArticle());
				ps.setInt(2, nouvelleEnchere.getNoUtilisateur());

				try(ResultSet rs = ps.executeQuery())
				{
					// Si l'utilisateur a déjà effectué une enchère, on va écraser la précédente
					if(rs.next())
					{
						ajoutEnchere = "UPDATE ENCHERES SET date_enchere=?,montant_enchere=? "
									 + "WHERE no_utilisateur=? AND no_article=?;";
					}
					// Sinon, on va en créer une nouvelle
					else
					{
						ajoutEnchere = "INSERT INTO ENCHERES (date_enchere,montant_enchere,no_utilisateur,no_article) "
									 + "VALUES (?,?,?,?);";
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la vérification d'existence d'une enchère : "+e.getMessage());
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : "+e);
			}
		
			// On exécute ensuite la requête correspondant à une mise à jour ou un ajout d'enchère
			try(PreparedStatement ps = connexion.prepareStatement(ajoutEnchere))
			{			
				ps.setTimestamp(1, nouvelleEnchere.getDateEnchere());
				ps.setInt(2, nouvelleEnchere.getMontantEnchere());
				ps.setInt(3, nouvelleEnchere.getNoUtilisateur());
				ps.setInt(4, nouvelleEnchere.getNoArticle());
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de l'ajout d'une enchère : " + e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : " + e);
		}
	}
	
	/**
	 *  Obtention de la derniere enchere sur un article.
	 * @param noArticle Le numéro de l'article
	 * @return Retourne l'enchère si elle a été trouvée.
	 * @throws DaoException Propagation de l'exception.
	 */
	public BeanEnchere derniereEnchere(int noArticle) throws DaoException
	{
		BeanEnchere enchereTrouvee = new BeanEnchere();
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(DERNIERE_ENCHERE))
			{
				ps.setInt(1, noArticle);
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next())
					{
						enchereTrouvee.setNoArticle(noArticle);
						enchereTrouvee.setDateEnchere(rs.getTimestamp("date_enchere"));
						enchereTrouvee.setMontantEnchere(rs.getInt("montant_enchere"));
						enchereTrouvee.setNoUtilisateur(rs.getInt("no_utilisateur"));
					}
					else
					{
						enchereTrouvee = null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la recherche de la dernière enchère pour l'article numéro "+noArticle
						+ " : "+e.getMessage());
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
		return enchereTrouvee;
	}
	
	/**
	 *  Listing de toutes les enchères
	 * @return Retourne soit une liste d'enchère, soit une valeur null.
	 * @throws DaoException Lever et propagation de l'exception.
	 */
	public List<BeanEnchere> listerEncheres() throws DaoException
	{
		List<BeanEnchere> listeResultats = new ArrayList<BeanEnchere>();
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(ResultSet rs = connexion.createStatement().executeQuery(LISTER_TOUT))
			{
				while(rs.next())
				{
					BeanEnchere enchereTrouvee = new BeanEnchere();
					enchereTrouvee.setNoUtilisateur(rs.getInt("no_utilisateur"));
					enchereTrouvee.setNoArticle(rs.getInt("no_article"));
					enchereTrouvee.setDateEnchere(rs.getTimestamp("date_enchere"));
					enchereTrouvee.setMontantEnchere(rs.getInt("montant_enchere"));
					
					// On ajoute l'enchere temporaire à la liste de résultats et on recommence
					listeResultats.add(enchereTrouvee);				
				}
			} 
			catch (SQLException e) 
			{
				throw new DaoException("Erreur lors du listing de toutes les enchères : " +e.getMessage());				
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
		return listeResultats;
	}
}
