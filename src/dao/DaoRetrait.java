package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.BeanRetrait;
import helper.AccesBase;

/**
 * Classe permettant d'ajouter ou de rechercher un point de retrait dans la base de données.
 * @author DWWM-Equipe #1
 */
public class DaoRetrait 
{
	private static final String AJOUTER_NOUVEAU = "INSERT INTO RETRAITS (no_article,rue,code_postal,ville) "
												+ "VALUES(?,?,?,?);";
	private static final String RECHERCHER_PAR_NUMERO = "SELECT * FROM RETRAITS WHERE no_article=?;";
	
	private static final String MODIFIER_RETRAIT = "UPDATE RETRAITS SET rue=?,code_postal=?,ville=? WHERE no_article=?;";
	/**
	 * Ajout d'un point de retrait.
	 * @param retrait Le point de retrait que l'on souhaite ajouter ou modifier.
	 * @throws DaoException Propage l'exception.
	 */
	public void ajouterRetrait(BeanRetrait retrait) throws DaoException
	{
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(AJOUTER_NOUVEAU))
			{
				ps.setInt(1, retrait.getNoArticle());
				ps.setString(2, retrait.getRue());
				ps.setString(3, retrait.getCodePostal());
				ps.setString(4, retrait.getVille());
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de l'ajout du point de retrait : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion � la BDD : " + e );
		}
	}
	
	/**
	 * Recherche d'un point de retrait.
	 * @param noArticle Le numéro de l'article par le lequel on va rechercher
	 * un point de retrait.
	 * @return Le point de retrait recherché.
	 * @throws DaoException Propage l'exception.
	 */
	public BeanRetrait obtenirRetrait(int noArticle) throws DaoException
	{
		BeanRetrait retraitTrouve = new BeanRetrait();
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(RECHERCHER_PAR_NUMERO))
			{
				ps.setInt(1, noArticle);
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next())
					{
						retraitTrouve.setNoArticle(noArticle);
						retraitTrouve.setRue(rs.getString("rue"));
						retraitTrouve.setCodePostal(rs.getString("code_postal"));
						retraitTrouve.setVille(rs.getString("ville"));
					}
					else
					{
						retraitTrouve = null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la recherche du point de retrait : "+e.getMessage());
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
		return retraitTrouve;
	}
	
	/**
	 * Modification des points de retraits.
	 * @param retrait Le point de retrait.
	 * @throws DaoException Exception propagée et gérée par la DaoException.
	 */
	public void modifierRetrait(BeanRetrait retrait) throws DaoException
	{
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(MODIFIER_RETRAIT))
			{
				
				ps.setString(1, retrait.getRue());
				ps.setString(2, retrait.getCodePostal());
				ps.setString(3, retrait.getVille());
				ps.setInt(4, retrait.getNoArticle());
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de l'ajout du point de retrait : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}
	
	public void supprimerRetrait(BeanRetrait retrait)
	{
		
	}
}
