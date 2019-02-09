package ca.okbutwhy.data;
import ca.marshan.data.DataSourceFactory;
//Author: Ecenur Oztorun
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;

public class GuestDAOFactory 
{
	public static GuestDAO getGuestDAO()
	{
		return new GuestData(DataSourceFactory.getDataSource());
	}
}
