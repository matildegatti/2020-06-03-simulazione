package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Map<Integer,Player> idMap;
	SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge> grafo;
	List<Player> giocatoriDream;
	double maxGradoLiberta;
	
	public Model() {
		dao=new PremierLeagueDAO();
		idMap=new HashMap<Integer,Player>();
		
		this.dao.listAllPlayers(idMap);
	}

	public void creaGrafo(double x) {
		grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);		

		//vertici
		Graphs.addAllVertices(grafo, this.dao.getVertici(idMap, x));
		
		List<Adiacenza> adiacenze=new ArrayList<Adiacenza>();
		adiacenze=this.dao.getAdiacenza(idMap);
		
		for(Adiacenza a:adiacenze) {
			if(grafo.containsVertex(a.p1) && grafo.containsVertex(a.p2) && a.peso!=0) {
				if(a.peso>0)
					Graphs.addEdgeWithVertices(grafo, a.p1, a.p2, a.peso);
				else if(a.getPeso()<0)
					Graphs.addEdgeWithVertices(grafo, a.p2, a.p1, -a.peso);
			}
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Player giocatoreMigliore() {
		if(grafo==null)
			return null;
		
		Player player=null;
		Integer sommauscenti;
		Integer maxDegree=Integer.MIN_VALUE;
		for(Player p:grafo.vertexSet()) {
			sommauscenti=this.grafo.outDegreeOf(p);
			if(sommauscenti>maxDegree) {
				player=p;
				maxDegree=sommauscenti;
			}
		}
		
		return player;
	}
	
	public List<Opponenti> avversariBattuti(){
		Player p=this.giocatoreMigliore();
		List<Opponenti> opponenti=new LinkedList<Opponenti>();
		
		for(DefaultWeightedEdge archi:this.grafo.outgoingEdgesOf(p)) {
			Player t=this.grafo.getEdgeTarget(archi);
			double peso=this.grafo.getEdgeWeight(archi);
			
			Opponenti o=new Opponenti(t,peso);
			opponenti.add(o);
		}
		Collections.sort(opponenti);
		return opponenti;
	}
	
	public Graph<Player,DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}

	public List<Player> dreamTeam(int k) {
		this.giocatoriDream=new LinkedList<Player>();
		List<Player> parziale=new LinkedList<Player>();
		this.maxGradoLiberta=Integer.MIN_VALUE;
		
		cerca(parziale,null,k);
		
		return this.giocatoriDream;
	}
	
	private void cerca(List<Player> parziale, Player giocatore, int k) {
		if(parziale.size()==k) {
			double gradoTeam=this.gradoLibertaTeam(parziale);
			if(gradoTeam>this.maxGradoLiberta) {
				this.maxGradoLiberta=gradoTeam;
				this.giocatoriDream=new LinkedList<>(parziale);
			}
			return;
		}
		
		if(parziale.size()==0) {
			for(Player p:this.grafo.vertexSet()) {
				parziale.add(p);
				cerca(parziale,p,k);
				parziale.remove(p);
			}
		}
		else {
			for(DefaultWeightedEdge e:grafo.incomingEdgesOf(giocatore)) {
				Player p=this.grafo.getEdgeSource(e);
				if(!parziale.contains(p)) {
					parziale.add(p);
					cerca(parziale,p,k);
					parziale.remove(p);
				}
			}
		}
		
	}

	public double gradoLiberta(Player giocatore) {
		double grado=0;
		double pesoUscenti=0;
		double pesoEntranti=0;
		
		for(DefaultWeightedEdge e:this.grafo.outgoingEdgesOf(giocatore)) {
			pesoUscenti+=this.grafo.getEdgeWeight(e);
		}
		for(DefaultWeightedEdge e:this.grafo.incomingEdgesOf(giocatore)) {
			pesoEntranti+=this.grafo.getEdgeWeight(e);
		}
		grado=pesoUscenti-pesoEntranti;
		return grado;
	}
	
	public double gradoLibertaTeam(List<Player> giocatori) {
		double grado=0;
		for(Player p:giocatori) {
			grado+=this.gradoLiberta(p);
		}
		
		return grado;
	}

	public double getGradoMax() {
		return this.maxGradoLiberta;
	}
}
