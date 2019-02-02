package bean;

import java.io.Serializable;

/**
 * Bean étant en relation a toutes les methodes d'achats.
 * On récupère les valeurs de chaque attribut par cette classe.
 * @author DWWM-Equipe #1
 * 
 * 
 */
public class BeanAchats implements Serializable
{
	private static final long serialVersionUID = -4976702687539906023L;
	
	private boolean encheresOuvertes;
	private boolean encheresEnCours;
	private boolean encheresRemportees;
	
	public BeanAchats() {};
	
	public BeanAchats(boolean encheresOuvertes, boolean encheresEnCours, boolean encheresRemportees)
	{
		this.encheresOuvertes = encheresOuvertes;
		this.encheresEnCours = encheresEnCours;
		this.encheresRemportees = encheresRemportees;
	}

	public boolean isEncheresOuvertes() 
	{
		return encheresOuvertes;
	}

	public void setEncheresOuvertes(boolean encheresOuvertes) 
	{
		this.encheresOuvertes = encheresOuvertes;
	}

	public boolean isEncheresEnCours() 
	{
		return encheresEnCours;
	}

	public void setEncheresEnCours(boolean encheresEnCours) 
	{
		this.encheresEnCours = encheresEnCours;
	}

	public boolean isEncheresRemportees() 
	{
		return encheresRemportees;
	}

	public void setEncheresRemportees(boolean encheresRemportees) 
	{
		this.encheresRemportees = encheresRemportees;
	}
}
