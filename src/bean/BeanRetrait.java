package bean;

import java.io.Serializable;

/**
 * Classe par laquelle on accorde les attributs des points de retrait.
 * @author DWWM-Equipe #1
 */
public class BeanRetrait implements Serializable
{
	private static final long serialVersionUID = 8261036066304705450L;
	
	// attributs
    private int noArticle;
    private BeanArticleVendu article;
    private String rue;
    private String codePostal;
    private String ville;

    // constructeurs
    public BeanRetrait()
    {}

    public BeanRetrait(int noArticle, BeanArticleVendu article, String rue, String codePostal, String ville)
    {
        this.noArticle = noArticle;
        this.article = article;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    // getters et setters
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

    public String getRue()
    {
        return rue;
    }

    public void setRue(String rue)
    {
        this.rue = rue;
    }

    public String getCodePostal()
    {
        return codePostal;
    }

    public void setCodePostal(String codePostal)
    {
        this.codePostal = codePostal;
    }

    public String getVille()
    {
        return ville;
    }

    public void setVille(String ville)
    {
        this.ville = ville;
    }
}
