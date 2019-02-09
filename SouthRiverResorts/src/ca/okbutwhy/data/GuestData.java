package ca.okbutwhy.data;
//Author: Ecenur Oztorun
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;

class GuestData implements GuestDAO 
{
	private DataSource ds;
	
	GuestData(DataSource ds)
	{
		super();
		setDs(ds);
	}
	
	private DataSource getDs()
	{
		return ds;
	}
	
	private void setDs(DataSource ds)
	{
		this.ds = ds;
	}
	
	@Override
	public Guest authenticateGuest(String username, String password) throws SQLException 
	{
		if (StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password))
		{
			try (Connection conn = getDs().getConnection())
			{
				try (PreparedStatement statement = conn.prepareStatement("SELECT id, firstname, lastname FROM guests WHERE username = ? AND password = ?"))
				{
					statement.setString(1, username);
					statement.setString(2, password);
					
					try (ResultSet rs = statement.executeQuery())
					{
						if (rs.next())
							return new Guest(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"));
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public void cancelReservation(int roomNo, int guestId) throws SQLException 
	{		
		try(Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT guestId, roomNo FROM reservations WHERE guestId = ? AND roomNo = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				statement.setInt(1, guestId);
				statement.setInt(2, roomNo);
				
				try (ResultSet rs = statement.executeQuery())
				{
					if(rs.next())
					{
						rs.deleteRow();
					}
				}
			}
		}
	}

	@Override
	public Guest getGuest(int guestId) throws SQLException 
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT id, firstname, lastname FROM guests WHERE id = ?"))
			{
				statement.setInt(1, guestId);
				
				try(ResultSet rs = statement.executeQuery())
				{
					if (rs.next())
					{
						return new Guest(rs.getInt("guestId"), rs.getString("firstname"), rs.getString("lastname"));
					}
					
					else
						return null;
				}
			}
		}
	}

	@Override
	public List<Reservation> getReservations(int guestId) throws SQLException 
	{		
		List<Reservation> reservations = new ArrayList<Reservation>();
		
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT roomNo FROM reservations WHERE guestId = ? ORDER BY roomNo ASC"))
			{
				statement.setInt(1, guestId);
				
				try (ResultSet rs = statement.executeQuery())
				{
					while(rs.next())
					{
						reservations.add(new Reservation(rs.getInt("roomNo"), guestId));
					}
					
					return reservations;
				}
			}
		}
	}

}
