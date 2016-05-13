package it.polito.tdp.porto.DAO;

import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnect {
	
	private static String jdbcURL="jdbc:mysql://localhost/porto?user=root";
	private static ComboPooledDataSource sourceData=null;
	
	
	public static Connection getConnection()
	{
		try
		{
			if(sourceData==null)
			{
				sourceData= new ComboPooledDataSource();
				sourceData.setJdbcUrl(jdbcURL);
			}
			return sourceData.getConnection();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Errore nella connessione al database");
		}
		
	}
}
