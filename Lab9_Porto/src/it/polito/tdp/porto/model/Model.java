package it.polito.tdp.porto.model;

import java.util.*;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.porto.DAO.ArticoloDAO;
import it.polito.tdp.porto.DAO.ConnessioniDAO;
import it.polito.tdp.porto.DAO.DBConnect;
import it.polito.tdp.porto.DAO.ScrittoreDAO;

public class Model {
	
	private List<Articolo> articoli= new ArrayList<Articolo>();
	private List<Autore> autori= new ArrayList<Autore>();
	private DefaultDirectedWeightedGraph<Autore, DefaultWeightedEdge> grafo= new DefaultDirectedWeightedGraph<Autore, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	List<List<Autore>> lista;
	List<Autore> visited;
	List<Autore> temp;
	
	public List<Autore> getScrittori()
	{
		ScrittoreDAO s=new ScrittoreDAO();
		autori=s.getScrittori();
		return autori;
	}

	public void creaGrafo()
	{
		ConnessioniDAO c= new ConnessioniDAO(); 
		//Creo i vertici
		for (Autore a : autori) {
			grafo.addVertex(a);
		}
		//Creo gli archi
		for (Articolo art : articoli) {
			c.getAutoriPerArticolo(art);
		for (Autore a : art.autori) {
			for (Autore e : art.autori) {
				if(a.getId_creator()!=e.getId_creator()&&!grafo.containsEdge(a,e))
				//grafo.addEdge(a,e);
					Graphs.addEdgeWithVertices(grafo, a, e, 1.0);
				Graphs.addEdgeWithVertices(grafo, e, a, 1.0);
			}
		}
		}
	}

	public void getArticoli() {
		ArticoloDAO a= new ArticoloDAO();
		articoli=a.getArticoli();
	}

	public void collega() {
		ConnessioniDAO c= new ConnessioniDAO();
		for (Autore a : autori) {
			a.articoli.addAll(c.getArticoliPerAutore(a));
			a.trovaCoautori();
		}
		
		for (Articolo a : articoli) {
			a.autori.addAll(c.getAutoriPerArticolo(a));
		}
		
	}

	public Autore getAutoreByID(String s) {
		for (Autore a : autori) {
			if (a.getId_creator()==Integer.parseInt(s))
				return a;
		}
		return null;
	}

	public List<Autore> getCoautori(Autore a) {
		List<Autore> lista=Graphs.neighborListOf(grafo, a);
		List<Autore> au=new ArrayList<Autore>();
		for (Autore e: lista) {
			if(!au.contains(e))
				au.add(e);
		}
		return au;
	}

	public List<List<Autore>> itera(Autore a){
		if(visited.size()==autori.size())
		{
			return lista;
		}
		
		GraphIterator<Autore, DefaultWeightedEdge> dfv= new DepthFirstIterator<Autore, DefaultWeightedEdge>(grafo, a);
		while(dfv.hasNext())
		{
			Autore auto=dfv.next();
			visited.add(auto);
			temp.add(auto);
		}
		lista.add(temp);
		temp.clear();
		for (Autore w : autori) {
			if(!visited.contains(w))
			{
				itera(w);
			}
		}
		return null;
	}
	
	public List<List<Autore>> cercaCluster() {
		lista= new ArrayList<List<Autore>>();
		visited= new ArrayList<Autore>();
		temp= new ArrayList<Autore>();
		for (Autore w : autori) {
			if(!visited.contains(w))
			{
				GraphIterator<Autore, DefaultWeightedEdge> dfv= new DepthFirstIterator<Autore, DefaultWeightedEdge>(grafo, w);
				while(dfv.hasNext())
				{
					Autore auto=dfv.next();
					visited.add(auto);
					temp.add(auto);
				}
				lista.add(new ArrayList<Autore>(temp));
				temp.clear();;
			}
		}
		return lista;
	}

	public List<Articolo> trovaCammini(Autore a, Autore b) {
		List<Autore> lis= getCoautori(a);
		List<Autore> lista=new ArrayList<Autore>();
		List<Articolo> articoli=new ArrayList<Articolo>();
		if(lis.contains(b))
		{
			System.out.println("Sono parenti diretti");
		}
		//Uso l'algoritmo per calcolare un cammino tra i due autori non coautori
		DijkstraShortestPath<Autore, DefaultWeightedEdge> dijkstra= new DijkstraShortestPath<Autore, DefaultWeightedEdge>(grafo, a, b);
		GraphPath<Autore, DefaultWeightedEdge> path= null;
		path=dijkstra.getPath();
		if(path!=null)
		{
			//ottengo i vertici
			lista= Graphs.getPathVertexList(path);
			//query al DB per conoscere gli articoli tra ogni coppia di vertici collegati
			for (int i = 0; i < lista.size(); i++) {
				if(i!=lista.size()-1)
				{
					ConnessioniDAO con= new ConnessioniDAO();
					articoli.addAll(con.trovaArticoliTraAutori(lista.get(i), lista.get(i+1)));	
				}
			}
			//Ho ottenuto tutti gli articoli che collegano ogni coppia di vertici
			return articoli;
		}
		else
			return null;
		
	}
}
