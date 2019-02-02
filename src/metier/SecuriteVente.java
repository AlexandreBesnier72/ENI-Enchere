package metier;

import bean.BeanArticleVendu;
import bean.BeanRetrait;
import bean.BeanUtilisateur;
import dao.DaoCategorie;
import dao.DaoException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Sécurité pour la vérification des articles.
 * @author DWWM-Equipe #1
 */
public class SecuriteVente
{
	/**
	 * Verifie les articles.
	 * @param articleVendu L'article vendu.
	 * @return Retourne un message d'erreur.
	 * @throws DaoException Propagation d'une exception.
	 */
    public static String verifArticle(BeanArticleVendu articleVendu) throws DaoException
    {
        String erreur = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateCourante = null;
        try
        {
	        try
	        {
	            dateCourante = sdf.parse( LocalDate.now().toString() );
	        }
	        catch (ParseException e)
	        {
	            System.err.println("Parsage de la date échoué.");
	        }
	        DaoCategorie daoCategorie = new DaoCategorie();
	        int nombreCategorie = daoCategorie.listerCategories().size();
	
	        // nom
	        if (articleVendu.getNomArticle().length() < 1)
	        {
	            erreur = "Nom de l'article trop court.";
	        }
	        else if(articleVendu.getNomArticle().length() > 50)
	        {
	            erreur = "Nom de l'article trop long.";
	        }
	
	        // description
	        if (articleVendu.getDescription().length() < 1)
	        {
	            erreur = "Description trop courte.";
	        }
	        else if(articleVendu.getDescription().length() > 300)
	        {
	            erreur = "La description ne peut excéder 500 caractères";
	        }
	
	        // date
	        Date dateDebut = null;
	        Date dateFin = null;
	        try
	        {
	            dateDebut = sdf.parse( articleVendu.getDateDebutEncheres().toString() );
	            dateFin = sdf.parse( articleVendu.getDateFinEncheres().toString() );
	        }
	        catch (ParseException e)
	        {
	            System.err.println("Parsage des dates échoué.");
	        }
	
	        if (dateDebut.compareTo(dateCourante) < 0)
	        {
	            erreur = "Impossible de mettre une date de début avant la date du jour";
	        }
	        else if(dateFin.compareTo(dateCourante) < 0)
	        {
	            erreur = "Impossible de mettre une date de fin avant la date du jour";
	        }
	        else if(dateDebut.compareTo(dateFin) >= 0)
	        {
	            erreur = "Impossible de mettre une date de fin avant la date du début";
	        }
	
	        // prix
	        if (articleVendu.getPrixInitial() < 0)
	        {
	            erreur = "Le prix ne peut pas être négatif";
	        }
	
	        // catégorie
	        if (articleVendu.getNoCategorie() < 1 || articleVendu.getNoCategorie() > nombreCategorie)
	        {
	            erreur = "La catégorie est invalide";
	        }
        }
        catch(DaoException e)
        {
        	throw new DaoException("Erreur lors de la vérification d'un article : "+e.getMessage());
        }
        return erreur;
    }
    
    /**
     * Vérifie les point de retraits.
     * @param retrait Le point de retrait.
     * @return Retourne une erreur en cas de levée d'une exception.
     */
    public static String verifRetrait(BeanRetrait retrait)
    {
        String erreur = "";

        if (retrait.getRue().length() < 1)
        {
            erreur = "Nom de rue trop court";
        }
        else if(retrait.getCodePostal().length() < 5)
        {
            erreur = "Code postal invalide";
        }
        else if(retrait.getVille().length() < 1)
        {
            erreur = "Nom de la ville trop court";
        }

        return erreur;
    }
    
    /**
     * Vérifie les montants des enchères.
     * @param montantEnchere Le montant de l'enchère.
     * @param articleVendu L'article de l'enchère.
     * @return Retourne une erreur en cas de levée d'une exception.
     */
    public static String verifMontantEnchere(int montantEnchere, BeanArticleVendu articleVendu, BeanUtilisateur utilisateur)
    {
        String erreur = null;
        if (montantEnchere < articleVendu.getPrixVente())
        {
            erreur = "Enchère trop faible.";
        }
        else if (utilisateur.getCredit() <= montantEnchere)
		{
			erreur = "Crédit insuffisant";
		}

        return erreur;
    }
}