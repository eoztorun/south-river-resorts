package ca.okbutwhy;

//Author: Ecenur Oztorun
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Hotel;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.marshan.*;
import ca.marshan.data.ReservationDAOFactory;

/**
 * Servlet Filter implementation class ReserveRoomFilter
 */

public class ReserveRoomFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public ReserveRoomFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException 
	{
		try
		{
			HttpServletRequest request = (HttpServletRequest)req;
			HttpServletResponse response = (HttpServletResponse)resp;
			Hotel hotel = (Hotel)request.getServletContext().getAttribute("hotel");
			HttpSession session = request.getSession();
			Guest currentGuest = (Guest)session.getAttribute("guest");
			
			if ("POST".equals(request.getMethod()) && request.getParameter("reserve") != null)
			{
				int floor;
				int unit;
					
				try
				{
					floor = Integer.parseInt(request.getParameter("floorNo"));
					unit = Integer.parseInt(request.getParameter("unitNo"));
				}
					
				catch(NumberFormatException nfe)
				{
					throw new RequestInvalidException();
				}
					
				if(floor < 1 || floor > hotel.getFloors())
					throw new RequestInvalidException();
				
				if(unit < 1 || unit > hotel.getRoomsPerFloor())
					throw new RequestInvalidException();
				
				int roomNo = (floor*100) + unit;
					
				synchronized(this)
				{
					if(!(ReservationDAOFactory.getReservationDAO().isReserved(roomNo)))
					{
						ReservationDAOFactory.getReservationDAO().createReservation(roomNo, currentGuest.getId());
						response.sendRedirect("reservations.jspx");
						return;
					}
				}				
			}
					
			else 
			{
				int floorNo;
				int unitNo;
					
				List<Reservation> resList = ReservationDAOFactory.getReservationDAO().getReservations();
				Reservation[][] resArray = new Reservation[hotel.getFloors()][hotel.getRoomsPerFloor()];
					
				for(Reservation r : resList)
				{
					unitNo = ((r.getRoomNo()) % 10);
					floorNo = (((r.getRoomNo()) - unitNo) / 100);
					resArray[floorNo-1][unitNo-1] = r;
				}
					
				request.setAttribute("floors", hotel.getFloors());
				request.setAttribute("units", hotel.getRoomsPerFloor());
				request.setAttribute("reservations", resArray);
				chain.doFilter(req, resp);						
			}	
		}
			
		catch (SQLException sqle)
		{
			throw new ServletException(sqle);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
