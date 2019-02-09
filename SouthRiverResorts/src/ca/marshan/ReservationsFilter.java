package ca.marshan;

//by: Martyn Whanslaw
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.marshan.data.ReservationDAOFactory;
import ca.okbutwhy.data.GuestDAOFactory;
import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.ReservationDAO;

/**
 * Servlet Filter implementation class ReservationsFilter
 */

/*
 * @WebFilter( dispatcherTypes = { DispatcherType.REQUEST,
 * DispatcherType.FORWARD } , urlPatterns = { "/ReservationsFilter",
 * "/reservations.jspx" })
 */

public class ReservationsFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public ReservationsFilter() {
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
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) req;

		HttpSession session = request.getSession();
		Guest currentGuest = (Guest) session.getAttribute("guest");
		String roomNum = request.getParameter("custRes");
		int roomNo;
		Reservation res = null;
		ReservationDAO resRoom = ReservationDAOFactory.getReservationDAO();

		try {
			List<Reservation> reservations = GuestDAOFactory.getGuestDAO()
					.getReservations(currentGuest.getId());

			if ("POST".equals(request.getMethod())
					&& request.getParameter("submit") != null) {
				try {
					roomNo = Integer.parseInt(roomNum);
				} catch (NumberFormatException nfe) {
					throw new RequestInvalidException(nfe);
				}
				if (StringHelper.isNullOrEmpty(roomNum))
					throw new RequestInvalidException();
				else {
					res = resRoom.getReservation(roomNo);

					if (res != null) {
						GuestDAOFactory.getGuestDAO().cancelReservation(roomNo,
								currentGuest.getId());
						reservations = GuestDAOFactory.getGuestDAO()
								.getReservations(currentGuest.getId());
					} else
						throw new RequestInvalidException();
				}
			}
			request.setAttribute("reservations", reservations);
			chain.doFilter(req, resp);
		} catch (SQLException sqle) {
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
