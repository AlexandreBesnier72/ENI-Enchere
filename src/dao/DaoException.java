package dao;

/**
 * Classe permettant de gérer toute les exceptions levées et propagées par les différentes DAO.
 * @author DWWM-Equipe #1
 */
public class DaoException extends Exception
{
	private static final long serialVersionUID = 1031847059109199548L;

	// Constructeurs
	public DaoException()
	{
		super();
	}
	
	public DaoException(String message)
	{
		super(message);
	}
	
	public DaoException(String message, Throwable exception)
	{
		super(message, exception);
	}
	
	
	@Override
	/**
	 * 
	 * @return Le message d'erreur
	 */
	public String getMessage()
	{
		StringBuffer sb = new StringBuffer("Couche DAO - ");
		sb.append(super.getMessage());
		
		return sb.toString();
	}
}