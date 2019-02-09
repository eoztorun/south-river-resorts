package ca.marshan;
//By: Martyn Whanslaw
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ca.okbutwhy.data.GuestDAOFactory;
import ca.on.senecac.prg556.senhotel.bean.Guest;

/**
 * Servlet Filter implementation class WelcomeFilter
 */
//@WebFilter(dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD }, urlPatterns = {"/welcome.jspx" })
public class WelcomeFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public WelcomeFilter() {
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
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;

		try 
		{
			HttpSession session = request.getSession();
			Guest currentGuest = (Guest)session.getAttribute("guest");
			
			if( null != currentGuest)
				response.sendRedirect(request.getContextPath() + "/");	
			
			else
			{
				String user = request.getParameter("userId");
				String pass = request.getParameter("userPass");

				Guest guest = GuestDAOFactory.getGuestDAO().authenticateGuest(user, pass);
				
				if ("POST".equals(request.getMethod()) && (null != guest))
				{
					session.setAttribute("guest", guest);
					response.sendRedirect(request.getContextPath() + "/reservations.jspx");					
				} 
				else if ("POST".equals(request.getMethod())&& null == guest)
				{
					request.setAttribute("invalidLogin", Boolean.TRUE);
					chain.doFilter(req, resp);
				}
				else
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
