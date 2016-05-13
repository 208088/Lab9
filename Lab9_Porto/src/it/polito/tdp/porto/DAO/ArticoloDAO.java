package it.polito.tdp.porto.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Articolo;

public class ArticoloDAO {
	
	public List<Articolo> getArticoli()
	{
		List<Articolo> lista= new ArrayList<Articolo>();
		Connection conn= DBConnect.getConnection();
		String sql="select eprintid, year, title from article;";
		try {
			PreparedStatement st=conn.prepareStatement(sql);
			ResultSet res=st.executeQuery();
			while (res.next())
			{
				Articolo a= new Articolo(Integer.parseInt(res.getString("eprintid")), Integer.parseInt(res.getString("year")), res.getString("title"));
				lista.add(a);
			}
			return lista;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Errore nella richiesta al database degli articoli.");
		}
	}

}
