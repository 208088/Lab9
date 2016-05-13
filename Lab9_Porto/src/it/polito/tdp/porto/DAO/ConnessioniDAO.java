package it.polito.tdp.porto.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import it.polito.tdp.porto.model.Articolo;
import it.polito.tdp.porto.model.Autore;

public class ConnessioniDAO {
		
		public List<Articolo> getArticoliPerAutore(Autore a)
		
		{
			List<Articolo> art=new ArrayList<Articolo>();
			Connection conn= DBConnect.getConnection();
			String sql="select a.eprintid AS t, title, year from authorship a, article e where a.eprintid=e.eprintid AND id_creator=?;";
			try {
				PreparedStatement st=conn.prepareStatement(sql);
				st.setInt(1, a.getId_creator());
				ResultSet res= st.executeQuery();
				while(res.next())
				{
					Articolo ar= new Articolo(Integer.parseInt(res.getString("t")), Integer.parseInt(res.getString("year")), res.getString("title"));
					art.add(ar);
				}
				conn.close();
				return art;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Errore nelle connessioni");
			}
		}

		public List<Autore> getAutoriPerArticolo(Articolo a) {
			List<Autore> art=new ArrayList<Autore>();
			Connection conn= DBConnect.getConnection();
			String sql="select id_creator AS t, family_name, given_name from creator where id_creator IN (select distinct id_creator from authorship where eprintid=?);";
			try {
				PreparedStatement st=conn.prepareStatement(sql);
				st.setInt(1, a.getEprintid());
				ResultSet res= st.executeQuery();
				while(res.next())
				{
					Autore ar= new Autore(Integer.parseInt(res.getString("t")), res.getString("family_name"), res.getString("given_name"));
					art.add(ar);
				}
				conn.close();
				a.getAutori().addAll(art);
				
				return art;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Errore nelle connessioni");
			}
		}

		public Collection<Articolo> trovaArticoliTraAutori(Autore a, Autore b) {
			List<Articolo> art=new ArrayList<Articolo>();
			Connection conn= DBConnect.getConnection();
			//Seleziono gli articoli in comune ai due autori
			String sql="select a.eprintid as t, art.title, year from authorship a, article art, authorship b where art.eprintid=a.eprintid and art.eprintid=b.eprintid and a.id_creator!=b.id_creator and a.id_creator=? and b.id_creator=?;";
			try {
				PreparedStatement st=conn.prepareStatement(sql);
				st.setInt(1, a.getId_creator());
				st.setInt(2, b.getId_creator());
				ResultSet res= st.executeQuery();
				while(res.next())
				{
					Articolo ar= new Articolo(Integer.parseInt(res.getString("t")), Integer.parseInt(res.getString("year")), res.getString("title"));
					art.add(ar);
				}
				conn.close();
				return art;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Errore nelle connessioni per trovare gli articoli in comune");
			}
		}
}
