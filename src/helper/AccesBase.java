package helper;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Classe qui permet de se connecter à la base de données.
 * @author DWWM-Equipe #1
 */
public class AccesBase
{
	/**
	 * Pool de connexion.
	 * @return Retourne une connexion.
	 * @throws SQLException Exception survenue lors d'une erreur avec la base de données.
	 */
	public static Connection dbConnexion() throws SQLException
	{
		Connection connexion = null;
		InitialContext context = null;
		DataSource dataSource= null;
		
		// Obtenir une référence sur le contexte initial JNDI
		try
		{
			context = new InitialContext();
		}
		catch(NamingException e)
		{
			throw new SQLException("Erreur d'accès au contexte initial JNDI");
		}
		
		// Recherche du pool de connexion sur l'annuaire
		try
		{
			dataSource = (DataSource) context.lookup( "java:comp/env/jdbc/dsEncheres" );
		} 
		catch (NamingException e)
		{
			throw new SQLException("Objet introuvable dans l'arbre JNDI:"+e.getMessage());
		}
		
		//Obtenir une connexion
		try
		{
			connexion = dataSource.getConnection();
			return connexion;
		} 
		catch (SQLException e)
		{
			throw new SQLException("Impossible d'obtenir une connexion:"+e.getMessage());
		}
	}

}
