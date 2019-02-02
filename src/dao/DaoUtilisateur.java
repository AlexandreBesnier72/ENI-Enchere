package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.BeanEnchere;
import bean.BeanUtilisateur;
import helper.AccesBase;

/**
 * Ajout, modification, recherche et suppression des utilisateurs dans la base de données.
 * @author DWWM-Equipe #1
 */
public class DaoUtilisateur 
{
	private static final String AJOUTER_NOUVEAU = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue,"
												+ " code_postal, ville, mot_de_passe, credit, administrateur)"
												+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String RECHERCHE_PAR_PSEUDO = "SELECT * FROM UTILISATEURS WHERE pseudo=?;";
	
	private static final String RECHERCHE_PAR_NUMERO = "SELECT * FROM UTILISATEURS WHERE no_utilisateur=?;";
	
	private static final String MODIFIER = "UPDATE UTILISATEURS SET pseudo = ?, nom = ?, prenom = ?, email = ?,"
											+ " telephone = ?, rue = ?, code_postal = ?, ville = ?, mot_de_passe = ?"
											+ " WHERE no_utilisateur = ?;";
	
	private static final String SUPPRIMER = "DELETE FROM UTILISATEURS WHERE pseudo=?;";
	
	private static final String CONNECTER = "SELECT * FROM UTILISATEURS "
			  								+ "WHERE (pseudo=? OR email=?) AND mot_de_passe=?;";
	
	private static final String AFFICHER_CREDIT = "SELECT credit FROM UTILISATEURS WHERE no_utilisateur=?;";
	
	private static final String REMBOURSER = "UPDATE UTILISATEURS SET credit+=? WHERE no_utilisateur=?;";
	
	private static final String DEBITER = "UPDATE UTILISATEURS SET credit-=? WHERE no_utilisateur=?;";
	
	private static final String VERIFIER_PSEUDO = "SELECT pseudo FROM UTILISATEURS WHERE pseudo=?;";
	
	private static final String VERIFIER_EMAIL = "SELECT email FROM UTILISATEURS WHERE email=?;";
	
	
	/**
	 * Ajout d'un utilisateur dans la base de données.
	 * @param utilisateur Utilisateur courant.
	 * @throws DaoException Propage l'exception.
	 */
	public static void ajouterUtilisateur(BeanUtilisateur utilisateur) throws DaoException
	{
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(AJOUTER_NOUVEAU))
			{
				ps.setString(1, utilisateur.getPseudo());
				ps.setString(2, utilisateur.getNom());
				ps.setString(3, utilisateur.getPrenom());
				ps.setString(4, utilisateur.getEmail());
				ps.setString(5, utilisateur.getTelephone());
				ps.setString(6, utilisateur.getRue());
				ps.setString(7, utilisateur.getCodePostal());
				ps.setString(8, utilisateur.getVille());
				ps.setString(9, utilisateur.getMotDePasse());
				// Le crédit par défaut d'un nouvel utilisateur est de 0 points
				ps.setInt(10, 0);
				// Un utilisateur enregistré par le formulaire standard n'est pas un admin, le statut administrateur
				// prend donc la valeur 0 par défaut (type SQL bit, 0 ou 1 ou NULL->1)
				ps.setInt(11, 0);
				
				ps.executeUpdate();
			}
			catch (SQLException e)
			{
				System.err.println("Erreur lors de l'ajout d'un utilisateur : " + e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}
	
	/**
	 * Récupération d'un utilisateur en utilisant son pseudo.
	 * @param pseudo Pseudo utilisateur par l'utilisateur.
	 * @return L'utilisateur recherché par son pseudo.
	 * @throws DaoException Propagation de l'exception.
	 */
	public static BeanUtilisateur utilisateurParPseudo(String pseudo) throws DaoException
	{
		// On créée un nouvel utilisateur temporaire 
		BeanUtilisateur utilisateurTrouve = new BeanUtilisateur();

		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(RECHERCHE_PAR_PSEUDO))
			{
				ps.setString(1, pseudo);
	
				try(ResultSet rs = ps.executeQuery())
				{
					// Si la requ�te renvoie un r�sultat, on hydrate l'utilisateur temporaire, sinon 
					// on le met � null
					if(rs.next())
					{
						mappingUtilisateur(utilisateurTrouve,rs);
					}
					else
					{
						utilisateurTrouve = null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la recherche d'un utilisateur par pseudo : " + e.getMessage() );
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : " + e);
			}		
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
		return utilisateurTrouve;
	}
	
	/**
	 * Récupération d'un utilisateur en utilisant son ID par la base de données.
	 * @param noUtilisateur ID de l'utilisateur courant.
	 * @return L'utilisateur recherché par son numéro.
	 * @throws DaoException Propage l'exception.
	 */
	public static BeanUtilisateur utilisateurParNumero(int noUtilisateur) throws DaoException
	{
		// On créé un nouvel utilisateur temporaire 
		BeanUtilisateur utilisateurTrouve = new BeanUtilisateur();
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(RECHERCHE_PAR_NUMERO))
			{
				ps.setInt(1, noUtilisateur);					
				
				try(ResultSet rs = ps.executeQuery())
				{
					// On hydrate l'utilisateur temporaire si la requ�te a renvoyé
					// des résultats, sinon il est mis à null
					if(rs.next())
					{
						mappingUtilisateur(utilisateurTrouve,rs);
					}
					else
					{
						utilisateurTrouve = null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la recherche d'un utilisateur par numéro : " + e.getMessage() );
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : " + e);
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
		return utilisateurTrouve;
	}	
	
	/**
	 * Modification d'un utilisateur dans la base de données.
	 * @param nouvelUtilisateur L'tilisateur courant ciblé par la modification.
	 * @throws DaoException Propage l'exception.
	 */
	public static void modifierUtilisateur(BeanUtilisateur nouvelUtilisateur) throws DaoException
	{
		// On récupère l'utilisateur courant avec les champs à modifier sauf le numéro d'utilisateur, on peut
		// donc utiliser ce numéro pour vérifier qu'on modifie le bon utilisateur		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(MODIFIER))
			{
				ps.setString(1, nouvelUtilisateur.getPseudo());
				ps.setString(2, nouvelUtilisateur.getNom());
				ps.setString(3, nouvelUtilisateur.getPrenom());
				ps.setString(4, nouvelUtilisateur.getEmail());
				ps.setString(5, nouvelUtilisateur.getTelephone());
				ps.setString(6, nouvelUtilisateur.getRue());
				ps.setString(7, nouvelUtilisateur.getCodePostal());
				ps.setString(8, nouvelUtilisateur.getVille());
				ps.setString(9, nouvelUtilisateur.getMotDePasse());
				
				// On vérifie le ciblage du bon utilisateur avec son numéro, puis on execute la modification 
				ps.setInt(10, nouvelUtilisateur.getNoUtilisateur());
				
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors de la modification de l'utilisateur : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}
	
	/**
	 *  Suppression d'un utilisateur de la base de données.
	 * @param utilisateur L'utilisateur ciblé par la suppression.
	 * @throws DaoException Propage l'exception.
	 */
	public static void supprimerUtilisateur( BeanUtilisateur utilisateur) throws DaoException
    {
        try(Connection connexion = AccesBase.dbConnexion())
		{
	        try( PreparedStatement ps = connexion.prepareStatement(SUPPRIMER))
	        {
	            ps.setString(1, utilisateur.getPseudo());
	            ps.executeUpdate();
	        } 
	        catch(SQLException e)
			{
				throw new DaoException("Erreur lors de la suppression de l'utilisateur : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
    }
	
	/**
	 *  Connexion d'un utilisateur en utilisant son pseudo/email et son mot de passe.
	 * @param identifiant Le pseudo de l'utilisateur.
	 * @param motDePasse Le mot de passe de l'utilisateur.
	 * @return L'utilisateur.
	 * @throws DaoException Propage l'exception.
	 */
	public static BeanUtilisateur connexionUtilisateur(String identifiant, String motDePasse) throws DaoException
	{
		// On créé un nouvel utilisateur temporaire 
		BeanUtilisateur utilisateurTrouve = new BeanUtilisateur();

		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(CONNECTER))
			{
				ps.setString(1, identifiant);
				ps.setString(2, identifiant);
				ps.setString(3, motDePasse);
	
				try(ResultSet rs = ps.executeQuery())
				{
					// On hydrate l'utilisateur temporaire si la requête a renvoy�
					// des résultats, sinon il est mis à null
					if(rs.next())
					{
						mappingUtilisateur(utilisateurTrouve,rs);
					}
					else
					{
						utilisateurTrouve = null;
					}
				}
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la connexion d'un utilisateur : " + e.getMessage() );
				}
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur de PreparedStatement : " + e);
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
		return utilisateurTrouve;
	}	
	
	/**
	 * Récupération du crédit actuel d'un utilisateur pour vérifier si il peut enchérir.
	 * @param utilisateur L'utilisateur courant sur lequel on effectue notre vérification.
	 * @return Les crédits de l'utilisateur.
	 * @throws DaoException Propagation de l'exception. 
	 */
	public static int afficherCredit(BeanUtilisateur utilisateur) throws DaoException
	{
		int creditUtilisateur = 0;
		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(AFFICHER_CREDIT))
			{
				ps.setInt(1, utilisateur.getNoUtilisateur());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next())
					{
						creditUtilisateur = rs.getInt("credit");
					}
					else
					{
						creditUtilisateur = -1;
					}
				}	
				catch(SQLException e)
				{
					throw new DaoException("Erreur lors de la vérification du crédit de l'utilisateur n° "+utilisateur.getNoUtilisateur()
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
		return creditUtilisateur;
	}
	
	/**
	 * Réatribution des crédits d'utilisateurs.
	 * @param derniereEnchere La dernière enchère effectuée.
	 * @throws DaoException Propage de l'exception.
	 */
	public static void recrediterUtilisateur(BeanEnchere derniereEnchere) throws DaoException
	{		
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(REMBOURSER))
			{
				ps.setInt(1, derniereEnchere.getMontantEnchere());
				ps.setInt(2, derniereEnchere.getNoUtilisateur());
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors du remboursement de l'utilisateur n° "+derniereEnchere.getNoUtilisateur()
				+ " : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}
	
	/**
	 *  Débite le compte d'un utilisateur suite à une enchère réussie.
	 * @param utilisateur L'utilisateur qui sera débité.
	 * @param montant_enchere Le montant de l'enchère.
	 * @throws DaoException Propage l'exception.
	 */
	public static void debiterUtilisateur(BeanUtilisateur utilisateur,int montant_enchere) throws DaoException
	{
		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(DEBITER))
			{
				ps.setInt(1, montant_enchere);
				ps.setInt(2, utilisateur.getNoUtilisateur());
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				throw new DaoException("Erreur lors du débit de l'utilisateur n° "+utilisateur.getNoUtilisateur() 
				+" : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
	}
	
	/**
	 * Vérification de l'existance du nom d'utilisateur dans la base de données.
	 * @param pseudo Le pseudo de l'utilisateur.
	 * @return Réponse de la vérification.
	 * @throws DaoException Propage l'exception.
	 */
	public static boolean isPseudoExistant(String pseudo) throws DaoException
	{
		boolean validation = true;		

		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(VERIFIER_PSEUDO))
			{
				ps.setString(1, pseudo);
				try(ResultSet rs = ps.executeQuery())
				{
					// Si la requête renvoie un résultat, cela veut dire que le pseudo testé
					// existe déjà, on valide donc le test.
					if(rs.next())
					{
						validation = true;
					}
					else
					{
						validation = false;
					}
				}
			}
			catch (SQLException e) 
			{
				throw new DaoException("Erreur lors de la vérification d'existence du pseudo : "+e.getMessage());
			}
		}
		catch(SQLException e)
		{
			throw new DaoException("Erreur de connexion à la BDD : "+e);
		}
		return validation;
	}
	
	/**
	 * Vérification de l'existance de l'email dans la base de données.
	 * @param email L'adresse mail de l'utilisateur.
	 * @return Réponse de la validation.
	 * @throws DaoException Propagation de l'exception. 
	 */
	public static boolean isEmailExistant(String email) throws DaoException
	{
		boolean validation = true;

		try(Connection connexion = AccesBase.dbConnexion())
		{
			try(PreparedStatement ps = connexion.prepareStatement(VERIFIER_EMAIL))
			{
				ps.setString(1, email);
				try(ResultSet rs = ps.executeQuery())
				{
					// Si la requête renvoie un résultat, cela veut dire que l'email
					// testé existe déjà, on valide donc le test.
					if(rs.next())
					{
						validation = true;
					}
					else
					{
						validation = false;
					}
				}
				catch (SQLException e)
				{
					throw new DaoException("Erreur lors de la v�rification d'existence de l'email : "+e.getMessage());
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
		return validation;
	}
	
	/**
	 * Vérification du mot de passe au format alphanumérique.
	 * @param motDePasse Le mot de passe de l'utilisateur.
	 * @return Réponse de la validation.
	 */
	public static boolean isMotDePasseFormatOk(String motDePasse)
	{
		boolean validation = false;

		if(motDePasse.matches("[a-zA-Z0-9]+"))
		{
			validation = true;
		}
		else
		{
			validation = false;
		}
		
		return validation;
	}
	
	/**
	 *  Méthode d'hydratation d'un nouvel objet Utilisateur.
	 * @param utilisateurTemporaire Utilisateur temporaire pour modifier les données de l'utilisateur ciblé.
	 * @param rs Récupération du téléphone de l'utilisateur.
	 * @return Un utilisateur temporaire.
	 * @throws SQLException Propage l'exception.
	 */
	private static void mappingUtilisateur(BeanUtilisateur utilisateurTemporaire, ResultSet rs) throws SQLException
	{
		utilisateurTemporaire.setNoUtilisateur(rs.getInt("no_utilisateur"));
		utilisateurTemporaire.setPseudo(rs.getString("pseudo"));
		utilisateurTemporaire.setNom(rs.getString("nom"));
		utilisateurTemporaire.setPrenom(rs.getString("prenom"));
		utilisateurTemporaire.setEmail(rs.getString("email"));
		if(rs.getString("telephone") != null)
		{
			utilisateurTemporaire.setTelephone(rs.getString("telephone"));
		}
		else
		{
			utilisateurTemporaire.setTelephone(null);
		}
		utilisateurTemporaire.setRue(rs.getString("rue"));
		utilisateurTemporaire.setCodePostal(rs.getString("code_postal"));
		utilisateurTemporaire.setVille(rs.getString("ville"));
		utilisateurTemporaire.setMotDePasse(rs.getString("mot_de_passe"));
		utilisateurTemporaire.setCredit(rs.getInt("credit"));
		utilisateurTemporaire.setAdministrateur(rs.getInt("administrateur"));	
	}
}
