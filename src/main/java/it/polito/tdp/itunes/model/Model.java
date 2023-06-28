package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	
	private List<Album> allAlbum;
	
	private Graph<Album, DefaultWeightedEdge> graph;
	
	
	private List<Album> soluzione;
	
	
	public Model() {
		
		this.dao = new ItunesDAO();
		
		this.allAlbum = new ArrayList<>();		
	}
	
	
	public String buildGraph(int n) {
		
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.loadNodes(n);
		
		Graphs.addAllVertices(this.graph, this.allAlbum);
		
		
		for(Album a1 : this.graph.vertexSet()) {
			for(Album a2 : this.graph.vertexSet()) {
				
				int peso = a1.getnSongs() - a2.getnSongs();
				
				
				if(peso > 0) { // a1 > a2
					
					Graphs.addEdgeWithVertices(this.graph, a2, a1, peso);
					
				} else if(peso < 0) { // a2 > a1
					
					Graphs.addEdgeWithVertices(this.graph, a1, a2, Math.abs(peso));
					
				}
				
				
				
			}
			
		}
		
		
		
		System.out.println(String.format("Grafo creato! \n# Vertici %d \n# Archi: %d \n", this.graph.vertexSet().size(), this.graph.edgeSet().size()));
		
		
		
		return String.format("Grafo creato! \n# Vertici %d \n# Archi: %d \n", this.graph.vertexSet().size(), this.graph.edgeSet().size());
		
	}
	
	private void loadNodes(int n) {
		
		//if(this.allAlbum.isEmpty())
			this.allAlbum = this.dao.getFilteredAlbums(n);
		
	}
	
	
	public List<Album> getAlbums() {
		
		List<Album> vertex = new ArrayList<Album>(this.allAlbum);
		
		Collections.sort(vertex);
		
		return vertex;
	}
	
	
	public int getNumVertex() {
		return this.graph.vertexSet().size();
	}
	
	public int getNumEdges() {
		return this.graph.edgeSet().size();
	}
	
	
	private int getBilancio(Album a) {
		
		int bilancio = 0;
		
		List<DefaultWeightedEdge> edgesIN = new ArrayList<>(this.graph.incomingEdgesOf(a));
		List<DefaultWeightedEdge> edgesOUT = new ArrayList<>(this.graph.outgoingEdgesOf(a));
		
		
		for(DefaultWeightedEdge e : edgesIN) {
			
			bilancio += this.graph.getEdgeWeight(e);
			
		}
		
		for(DefaultWeightedEdge e : edgesOUT) {
			
			bilancio -= this.graph.getEdgeWeight(e);
			
		}
		
		return bilancio;
	}
	
	
	public List<BilancioAlbum> getAdiacenti(Album album) {
		
		List<BilancioAlbum> result = new ArrayList<>();
		
		List<Album> successori = Graphs.successorListOf(this.graph, album);
		
		for(Album a : successori) {
			
			BilancioAlbum ba = new BilancioAlbum(a, this.getBilancio(a));
			
			result.add(ba);
			
		}
		
		Collections.sort(result);
		
		return result;
		
	}
	
	
	public List<Album> trovaPercorso(Album partenza, Album arrivo, int peso) {
		
		// Inizializzazione
		List<Album> parziale = new ArrayList<>();
		this.soluzione = new ArrayList<>();
		
		
		// Inizio ricorsione
		parziale.add(partenza);
		
		
		cerca(parziale, arrivo, peso);
		
		
		return this.soluzione;
	}


	private void cerca(List<Album> parziale, Album arrivo, int peso) {
		// TODO Auto-generated method stub
		
		Album ultimo = parziale.get(parziale.size()-1);
		
		// Condizione di terminazione
		if(ultimo.equals(arrivo) && this.getScore(parziale) > this.getScore(this.soluzione)) {
			
			this.soluzione = new ArrayList<>(parziale);
			
		}
		
		List<Album> successori = Graphs.successorListOf(this.graph, ultimo);
		
		for(Album a : successori) {
			
			if(!parziale.contains(a)) {
				
				DefaultWeightedEdge e = this.graph.getEdge(ultimo, a);
				
				if(this.graph.getEdgeWeight(e) > peso) {
					
					parziale.add(a);
					
					cerca(parziale, arrivo, peso);
					
					parziale.remove(parziale.size()-1);
					
				}
				
			}
			
		}
		
	}
	
	
	private int getScore(List<Album> parziale) {
		
		int score = 0;
		
		if(parziale.size() > 0) {
			
			Album source = parziale.get(0);
			
			for(Album a : parziale.subList(1, parziale.size()-1)) {
				
				if(getBilancio(a) > getBilancio(source)) {
					score += 1;
				}
				
			}
			
		}
		
		
		return score;
	}
	
	
}
