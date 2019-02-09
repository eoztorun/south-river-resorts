package ca.okbutwhy;
//Author: Ecenur Oztorun
import java.io.IOException;
import java.sql.SQLException;

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

import ca.okbutwhy.data.GuestDAOFactory;
import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;

/**
 * Servlet Filter implementation class LoginValidationFilter
 */
public class LoginValidationFilter implements Filter {

    /**
     * Default constructor. 
     */
	
	private String loginPage = "/index.jspx";
	
	
    public LoginValidationFilter() {
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
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		String uriLogin = request.getContextPath() + getLoginPage();
		
		if ((null == request.getSession().getAttribute("guest")) && (!uriLogin.equals(request.getRequestURI())))
			response.sendRedirect(uriLogin);
		
		else
			chain.doFilter(req, resp);
			
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		
		if(StringHelper.isNotNullOrEmpty(fConfig.getInitParameter("welcome")))
			setLoginPage(StringHelper.stringPrefix(fConfig.getInitParameter("welcome"), "/"));
	}
	
	public synchronized String getLoginPage()
	{
		return loginPage;
	}
	
	public synchronized void setLoginPage(String loginPage)
	{
		this.loginPage = loginPage;
	}

}
