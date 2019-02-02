package bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * Bean étant en relation avec tout les articles vendus.
 * C'est par cette classe qu'on récupère les valeurs de chaque
 * attribut de nos objets d'Articles.
 * @author DWWM-Equipe #1
 */
public class BeanArticleVendu implements Serializable
{
	private static final long serialVersionUID = 6254231237048020620L;
	
	// attributs
    private int noArticle;
    private String nomArticle;
    private String description;
    private Date dateDebutEncheres;
    private Date dateFinEncheres;
    private int prixInitial;
    private int prixVente;
    private int noUtilisateur;
    private BeanUtilisateur utilisateur;
    private int noCategorie;
    private BeanCategorie categorie;

    // constructeurs
    public BeanArticleVendu()
    {}

    public BeanArticleVendu(int noArticle, String nomArticle, String description, Date dateDebutEncheres, Date dateFinEncheres, int prixInitial, int prixVente, int noUtilisateur, BeanUtilisateur utilisateur, int noCategorie, BeanCategorie categorie)
    {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.noUtilisateur = noUtilisateur;
        this.utilisateur = utilisateur;
        this.noCategorie = noCategorie;
        this.categorie = categorie;
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

    public String getNomArticle()
    {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle)
    {
        this.nomArticle = nomArticle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDateDebutEncheres()
    {
        return dateDebutEncheres;
    }

    public void setDateDebutEncheres(Date dateDebutEncheres)
    {
        this.dateDebutEncheres = dateDebutEncheres;
    }

    public Date getDateFinEncheres()
    {
        return dateFinEncheres;
    }

    public void setDateFinEncheres(Date dateFinEncheres)
    {
        this.dateFinEncheres = dateFinEncheres;
    }

    public int getPrixInitial()
    {
        return prixInitial;
    }

    public void setPrixInitial(int prixInitial)
    {
        this.prixInitial = prixInitial;
    }

    public int getNoUtilisateur()
    {
        return noUtilisateur;
    }

    public void setNoUtilisateur(int noUtilisateur)
    {
        this.noUtilisateur = noUtilisateur;
    }

    public BeanUtilisateur getUtilisateur()
    {
        return utilisateur;
    }

    public void setUtilisateur(BeanUtilisateur utilisateur)
    {
        this.utilisateur = utilisateur;
    }

    public int getNoCategorie()
    {
        return noCategorie;
    }

    public void setNoCategorie(int noCategorie)
    {
        this.noCategorie = noCategorie;
    }

    public BeanCategorie getCategorie()
    {
        return categorie;
    }

    public void setCategorie(BeanCategorie categorie)
    {
        this.categorie = categorie;
    }

	public int getPrixVente() 
	{
		return prixVente;
	}

	public void setPrixVente(int prixVente) 
	{
		this.prixVente = prixVente;
	}
}
