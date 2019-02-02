package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.BeanCategorie;
import helper.AccesBase;

/**
 * Récupération des catégories à partir de la base de données et à partir de leur numéro respectif et leur libellé.
 * @author DWWM-Equipe #1
 */
public class DaoCategorie 
{
	// D�finition des requ�tes DAO
	private static final String LISTER_TOUT = "SELECT * FROM CATEGORIES;";
	private static final String LISTER_PAR_NUMERO = "SELECT * FROM CATEGORIES WHERE no_categorie=?;";
	
	
	/**
	 * Méthode permettant de lister les catégories d'un article.
	 * @return listeCategories Liste des catégories.
	 * @throws DaoException Propagation de l'exception.
	 */
	public List<BeanCategorie> listerCategories() throws DaoException
	{
		List<BeanCategorie> listeCategories = new ArrayList<BeanCategorie>();
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(ResultSet rs = connexion.createStatement().executeQuery(LISTER_TOUT))
			{
				while(rs.next())
				{
					// Tant que la requete renvoie un résultat, on créé un objet temporaire de type BeanCategorie
					// et on hydrate ses propriétés avec les r�sultats de la requête
					BeanCategorie categorieTrouvee = new BeanCategorie();
					categorieTrouvee.setNoCategorie(rs.getInt("no_categorie"));
					categorieTrouvee.setLibelle(rs.getString("libelle"));
					
					// On ajoute la catégorie temporaire à la liste de résultats
					listeCategories.add(categorieTrouvee);
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de la récupération de toutes les catégories : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion "+e);
		}
		return listeCategories;
	}
	
	
	/**
	 * Méthode de récupération des catégories par numéro.
	 * 
	 * @param noCategorie Le numéro de catégorie.
	 * @return Les catégories.
	 * @throws DaoException Propage l'exception.
	 */
	public BeanCategorie categorieParNumero(int noCategorie) throws DaoException
	{
		
		BeanCategorie categorieTrouvee = new BeanCategorie();
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(LISTER_PAR_NUMERO))
			{
				ps.setInt(1, noCategorie);
				try(ResultSet rs = ps.executeQuery())
				{
					// Si la catégorie recherchée existe, on hydrate l'objet temporaire
					// et on le renvoie
					if(rs.next())
					{
						categorieTrouvee.setNoCategorie(noCategorie);
						categorieTrouvee.setLibelle(rs.getString("libelle"));
					}
					else
					{
						categorieTrouvee =  null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de l'obtention de la catégorie par numéro : "+ e.getMessage());
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : "+ e);
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion : "+e);
		}
		
		return categorieTrouvee;
	}
}
