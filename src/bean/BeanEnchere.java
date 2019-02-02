package bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Classe par laquelle on récupère les valeurs des attributs des enchères.
 * @author DWWM-Equipe #1
 */
public class BeanEnchere implements Serializable
{
	private static final long serialVersionUID = -8447902608722880409L;
	
	// attributs
    private int noUtilisateur;
    private int noArticle;
    private BeanArticleVendu article;
    private Timestamp dateEnchere;
    private int montantEnchere;

    // constructeurs
    public BeanEnchere()
    {}

    public BeanEnchere(int noUtilisateur, int noArticle, BeanArticleVendu article, Timestamp dateEnchere, int montantEnchere)
    {
        this.noUtilisateur = noUtilisateur;
        this.noArticle = noArticle;
        this.article = article;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    //getters et setters

    public int getNoUtilisateur()
    {
        return noUtilisateur;
    }
    public void setNoUtilisateur(int noUtilisateur)
    {
        this.noUtilisateur = noUtilisateur;
    }

    public int getNoArticle()
    {
        return noArticle;
    }

    public void setNoArticle(int noArticle)
    {
        this.noArticle = noArticle;
    }

    public BeanArticleVendu getArticle()
    {
        return article;
    }

    public void setArticle(BeanArticleVendu article)
    {
        this.article = article;
    }

    public Timestamp getDateEnchere()
    {
        return dateEnchere;
    }

    public void setDateEnchere(Timestamp dateEnchere)
    {
        this.dateEnchere = dateEnchere;
    }

    public int getMontantEnchere()
    {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere)
    {
        this.montantEnchere = montantEnchere;
    }
}
