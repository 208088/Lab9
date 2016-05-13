package it.polito.tdp.porto.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Autore;

public class ScrittoreDAO {
	
	public List<Autore> getScrittori()
	{
		try
		{
			List<Autore> lista= new ArrayList<Autore>();
			Connection conn=DBConnect.getConnection();
			String sql="select id_creator, family_name, given_name from creator;";
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet res= st.executeQuery();
			while( res.next() )
			{
				Autore s= new Autore (Integer.parseInt(res.getString("id_creator")), res.getString("family_name"), res.getString("given_name"));
				lista.add(s);
			}
			return lista;
		} catch(SQLException e)
		{
			throw new RuntimeException("Errore nella query  per gli scrittori");
		}
	}

}
