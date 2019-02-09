package ca.marshan.data;
//By: Martyn Whanslaw
import ca.on.senecac.prg556.senhotel.dao.ReservationDAO;

public class ReservationDAOFactory {

	public static ReservationDAO getReservationDAO() {
		
		return new ReservationData(DataSourceFactory.getDataSource());
	}

}
