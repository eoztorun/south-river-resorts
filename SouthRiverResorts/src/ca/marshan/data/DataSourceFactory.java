package ca.marshan.data;

import javax.sql.DataSource;

public class DataSourceFactory {

	private static DataSource dataSource;
	
	public DataSourceFactory() {
		// TODO Auto-generated constructor stub
	}

	public static DataSource getDataSource()
	{
		return dataSource;
	}
	
	public static void setDataSource(DataSource dataSource)
	{
		DataSourceFactory.dataSource = dataSource;
		
		
	}
	
}
