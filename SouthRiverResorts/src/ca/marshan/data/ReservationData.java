package ca.marshan.data;

//By: Martyn Whanslaw
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.ReservationDAO;

class ReservationData implements ReservationDAO {

	private DataSource ds;

	ReservationData(DataSource ds) {
		super();
		setDs(ds);
	}

	private DataSource getDs() {
		return ds;
	}

	private void setDs(DataSource ds) {
		this.ds = ds;

	}

	@Override
	public Reservation createReservation(int roomNo, int guestId)
			throws SQLException {
		// This method inserts a new reservation into the Reservations table
		// with the specified room number and guest id.
		// The method then creates a new Reservation object with the room number
		// and guest id and returns it.

		try (Connection conn = getDs().getConnection()) 
		{
			try (Statement statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) 
			{
				try (ResultSet rs = statement.executeQuery("SELECT guestId, roomno FROM Reservations")) 
				{
					rs.moveToInsertRow();
					rs.updateInt("guestId", guestId);
					rs.updateInt("roomno", roomNo);
					rs.insertRow();

				}

			}
		}

		return new Reservation(roomNo, guestId);
	}

	@Override
	public Reservation getReservation(int roomNo) throws SQLException {
		// The method will search for the reservation with the specified room
		// number in the Reservations table and, if found, will return a
		// Reservation object populated with the room number and guest id of the
		// reservation.
		// If a reservation for the specified room number was not found in the
		// Reservations table, then the method simply returns null.

		try (Connection conn = getDs().getConnection()) {
			try (PreparedStatement statement = conn
					.prepareStatement("SELECT guestId, roomno FROM Reservations WHERE roomno = ?")) {
				statement.setInt(1, roomNo);

				try (ResultSet rs = statement.executeQuery()) {
					if (rs.next()) {

						return new Reservation(rs.getInt("roomNo"),
								rs.getInt("guestid"));
					} else {
						return null;
					}

				}

			}
		}
	}

	@Override
	public List<Reservation> getReservations() throws SQLException {
		// Returns a list of Reservation objects representing all of the
		// reservations in the database where each Reservation object is
		// populated with the reservation details as described for the
		// ReservationData.getReservation method above. If there are no
		// reservations, the method simply returns an empty list.

		List<Reservation> reservations = new ArrayList<Reservation>();
		
		try (Connection conn = getDs().getConnection()) 
		{
			try (PreparedStatement statement = conn
					.prepareStatement("SELECT guestId, roomno FROM Reservations")) {

				try (ResultSet rs = statement.executeQuery()) {
					while(rs.next()) 
					{
						reservations.add(new Reservation(rs.getInt("roomNo"),rs.getInt("guestid")));
					} 
					return reservations;
				}

			}
		}
		
	}

	@Override
	public boolean isReserved(int roomNo) throws SQLException {
		// The method will search for the reservation with the specified room
		// number in the Reservations table and, if found, will return true.
		// If a reservation for the specified room number was not found in the
		// Reservations table, then the method simply returns false.

		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn
					.prepareStatement("SELECT guestId, roomno FROM Reservations WHERE roomno = ?"))
			{
				statement.setInt(1, roomNo);

				try (ResultSet rs = statement.executeQuery())
				{
					if (rs.next())
					{

						return true;
					} 
					else
					{
						return false;
					}

				}

			}
		}
	}

}
