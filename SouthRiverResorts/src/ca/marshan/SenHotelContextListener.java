package ca.marshan;
//By: Martyn Whanslaw

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import ca.marshan.data.DataSourceFactory;
import ca.on.senecac.prg556.senhotel.bean.Hotel;

/**
 * Application Lifecycle Listener implementation class SenHotelContextListener
 *
 */
@WebListener
public class SenHotelContextListener implements ServletContextListener {
	 
    /**
     * Default constructor. 
     */
	@Resource(name="SenHotelDS")
	private DataSource ds;
	
    public SenHotelContextListener() {
        // TODO Auto-generated constructor stub 
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	DataSourceFactory.setDataSource(null);
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent contextEvent) {
        // TODO Auto-generated method stub
    
    	DataSourceFactory.setDataSource(ds); 
    	
    	String hotelName = contextEvent.getServletContext().getInitParameter("hotelName"); 
    	String amountOfFloors = contextEvent.getServletContext().getInitParameter("amountOfFloors"); 
    	String roomsPerFloor = contextEvent.getServletContext().getInitParameter("roomsPerFloor"); 

    	Hotel hotel = new Hotel(hotelName,(Integer.parseInt(amountOfFloors)),(Integer.parseInt(roomsPerFloor)));
    	contextEvent.getServletContext().setAttribute("hotel", hotel);    	
    }
	
}

