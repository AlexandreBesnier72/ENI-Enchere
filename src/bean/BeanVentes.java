package bean;

import java.io.Serializable;

/**
 * Classe qui nous permet de classer les attributs des ventes.
 * @author DWWM-Equipe #1
 */
public class BeanVentes implements Serializable
{
	private static final long serialVersionUID = 167975878445393833L;
	
	private boolean ventesEnCours;
	private boolean ventesNonDebutees;
	private boolean ventesTerminees;

	public BeanVentes() {};
	
	public BeanVentes(boolean ventesEnCours, boolean ventesNonDebutees, boolean ventesTerminees)
	{
		this.ventesEnCours = ventesEnCours;
		this.ventesNonDebutees = ventesNonDebutees;
		this.ventesTerminees = ventesTerminees;
	}

	public boolean isVentesEnCours() 
	{
		return ventesEnCours;
	}

	public void setVentesEnCours(boolean ventesEnCours) 
	{
		this.ventesEnCours = ventesEnCours;
	}

	public boolean isVentesNonDebutees() 
	{
		return ventesNonDebutees;
	}

	public void setVentesNonDebutees(boolean ventesNonDebutees) 
	{
		this.ventesNonDebutees = ventesNonDebutees;
	}

	public boolean isVentesTerminees() 
	{
		return ventesTerminees;
	}

	public void setVentesTerminees(boolean ventesTerminees) 
	{
		this.ventesTerminees = ventesTerminees;
	}
}
