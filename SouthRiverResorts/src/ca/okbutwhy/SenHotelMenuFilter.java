package ca.okbutwhy;
//Author: Ecenur Oztorun
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;
import ca.okbutwhy.data.*;

/**
 * Servlet Filter implementation class SenHotelMenuFilter
 */

public class SenHotelMenuFilter implements Filter {

    /**
     * Default constructor. 
     */
    public SenHotelMenuFilter() {
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
			Guest currentGuest = ((Guest)request.getSession().getAttribute("guest"));
			
			List<Reservation> reservations = GuestDAOFactory.getGuestDAO().getReservations(currentGuest.getId());
			
			request.setAttribute("nRooms", reservations.size());
			request.setAttribute("firstName", currentGuest.getFirstName());
			request.setAttribute("lastName", currentGuest.getLastName());
			
			chain.doFilter(req, resp);
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
