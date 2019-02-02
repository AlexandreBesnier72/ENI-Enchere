package bean;

import java.io.Serializable;

import bean.BeanAchats;
import bean.BeanVentes;

/**
 * Classe par laquelle ont récupère nos éléments que nous recherchons.
 * @author DWWM-Equipe #1
 */
public class BeanFiltreRecherche implements Serializable
{
	private static final long serialVersionUID = 7582307347896140076L;
	
	private String nomArticle;
	private int noCategorie;
	private BeanAchats achats;
	private BeanVentes ventes;
	
	public BeanFiltreRecherche() {};
	
	public BeanFiltreRecherche(String nomArticle, int noCategorie, BeanAchats achats, BeanVentes ventes)
	{
		this.nomArticle = nomArticle;
		this.noCategorie = noCategorie;
		this.achats = achats;
		this.ventes = ventes;
	}

	public String getNomArticle() 
	{
		return nomArticle;
	}

	public void setNomArticle(String nomArticle) 
	{
		this.nomArticle = nomArticle;
	}

	public int getNoCategorie() 
	{
		return noCategorie;
	}

	public void setNoCategorie(int noCategorie) 
	{
		this.noCategorie = noCategorie;
	}

	public BeanAchats getAchats() 
	{
		return achats;
	}

	public void setAchats(BeanAchats achats) 
	{
		this.achats = achats;
	}

	public BeanVentes getVentes() 
	{
		return ventes;
	}

	public void setVentes(BeanVentes ventes) 
	{
		this.ventes = ventes;
	}
}
