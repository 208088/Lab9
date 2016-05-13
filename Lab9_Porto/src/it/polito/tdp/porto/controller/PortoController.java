package it.polito.tdp.porto.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Articolo;
import it.polito.tdp.porto.model.Autore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model= new Model();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> combo1;

    @FXML
    private ComboBox<String> combo2;

    @FXML
    private Button btnCoautori;

    @FXML
    private Button btnCluster;

    @FXML
    private Button btnArticoli;

    @FXML
    private TextArea txtArea;

    @FXML
    void CercaArticoli(ActionEvent event) {
    	txtArea.clear();
    	String g=combo1.getValue();
		StringTokenizer st= new StringTokenizer(g, " ");
		Autore a= model.getAutoreByID(st.nextToken());
		String g1=combo2.getValue();
		StringTokenizer s= new StringTokenizer(g1, " ");
		Autore b= model.getAutoreByID(s.nextToken());
		try{
			List<Articolo> list= model.trovaCammini(a, b);
			if(list!=null)
			{
				for (Articolo au : list) {
					txtArea.appendText(au.getTitle()+"\n");
				}
			}
			else
				txtArea.setText("I due autori selezionati non risultano collegati");
		} catch(RuntimeException e){
			txtArea.setText("si e' verificato un errore durante la ricerca degli articoli");
		}
		
    }

    @FXML
    void cercaCluster(ActionEvent event) {
    	List<List<Autore>> lista= model.cercaCluster();
    	int i=1;
    	for (List<Autore> l : lista) {
			txtArea.appendText("Cluster "+i+": \n");
			i++;
			for (Autore a : l) {
				txtArea.appendText("-"+a.getCognome()+" "+a.getNome());
			}
			txtArea.appendText("\n");
		}
    }

    @FXML
    void cercaCoautori(ActionEvent event) {
    	if(combo1.getValue()!=null)
    	{	String g=combo1.getValue();
    		StringTokenizer st= new StringTokenizer(g, " ");
    		Autore a= model.getAutoreByID(st.nextToken());
    		List<Autore> coautori= model.getCoautori(a);
    		Collections.sort(coautori, new Comparator<Autore>(){

				@Override
				public int compare(Autore o1, Autore o2) {
					return o1.getCognome().compareTo(o2.getCognome());
				}
    			
    		});
    		txtArea.clear();
    		txtArea.appendText("Lista di coautori di "+ a.getCognome()+" "+a.getNome()+"\n");
    		for (Autore au : coautori) {
    			if(!au.equals(a))
    				txtArea.appendText("-"+au.getCognome()+" "+au.getNome()+"\n");
			}
    	}
    	else if(combo2.getValue()!=null)
    	{	String g=combo1.getValue();
    		StringTokenizer st= new StringTokenizer(g, " ");
    		Autore a= model.getAutoreByID(st.nextToken());
    		List<Autore> coautori= model.getCoautori(a);
    		txtArea.clear();
    		txtArea.appendText("Lista di coautori di "+ a.getCognome()+" "+a.getNome()+"\n");
    		for (Autore au : coautori) {
				txtArea.appendText("-"+au.getCognome()+" "+au.getNome()+"\n");
			}
    	}
    	else txtArea.setText("Seleziona un autore");
    }

    @FXML
    void initialize() {
        assert combo1 != null : "fx:id=\"combo1\" was not injected: check your FXML file 'Porto.fxml'.";
        assert combo2 != null : "fx:id=\"combo2\" was not injected: check your FXML file 'Porto.fxml'.";
        assert btnCoautori != null : "fx:id=\"btnCoautori\" was not injected: check your FXML file 'Porto.fxml'.";
        assert btnCluster != null : "fx:id=\"btnCluster\" was not injected: check your FXML file 'Porto.fxml'.";
        assert btnArticoli != null : "fx:id=\"btnArticoli\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtArea != null : "fx:id=\"txtArea\" was not injected: check your FXML file 'Porto.fxml'.";

        List<Autore> list= model.getScrittori();
        Collections.sort(list, new Comparator<Autore>(){

			@Override
			public int compare(Autore o1, Autore o2) {
				return o1.getCognome().compareTo(o2.getCognome());
			}
        	
        });
        for (Autore e : list) {
            combo1.getItems().add(e.getId_creator()+" "+ e.getNome()+" "+e.getCognome());
            combo2.getItems().add(e.getId_creator()+" "+e.getNome()+" "+e.getCognome());
		}
        model.getArticoli();
        model.creaGrafo();
    }
}
