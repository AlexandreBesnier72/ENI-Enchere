package bean;

import java.io.Serializable;

/**
 * Bean étant en relation avec les catégories.
 * C'est par cette classe qu'on récupère les attributs de nos objets Catégories.
 * @author DWWM-Equipe #1
 */
public class BeanCategorie implements Serializable
{
	private static final long serialVersionUID = -5129834350120960193L;
	
	// attributs
    private int noCategorie;
    private String libelle;

    // constructeurs
    public BeanCategorie()
    {}

    // getters et setters
    public BeanCategorie(int noCategorie, String libelle)
    {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }

    public int getNoCategorie()
    {
        return noCategorie;
    }

    public void setNoCategorie(int noCategorie)
    {
        this.noCategorie = noCategorie;
    }

    public String getLibelle()
    {
        return libelle;
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }
}
