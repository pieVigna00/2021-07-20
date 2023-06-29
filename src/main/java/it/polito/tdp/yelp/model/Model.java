package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	Graph<User, DefaultWeightedEdge> grafo;
	List<Arco> archi;
	Map<String, User> mappaUser;
	public Model() {
		this.dao= new YelpDao();
		mappaUser= new HashMap<>();
		this.dao.getAllUsers(mappaUser);
	}
	public void buildGraph(int numRece, int anno) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<User> vertici= this.dao.getVertici(numRece, mappaUser);
		Graphs.addAllVertices(this.grafo, vertici);
		archi= new ArrayList<>(this.dao.getArchi(mappaUser, anno));
		for(Arco a: archi) {
			if(this.grafo.containsVertex(a.getUtente1()) && this.grafo.containsVertex(a.getUtente2()))
			Graphs.addEdge(this.grafo, a.getUtente1(), a.getUtente2(), a.getGrado());
		}
		
	}
	public List<User> getSimile(User u){
		int max=0;
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if(max<this.grafo.getEdgeWeight(e)) {
				max=(int) this.grafo.getEdgeWeight(e);
			}
		}
		List<User> result= new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)==max) {
				User u2=Graphs.getOppositeVertex(grafo, e, u);
				result.add(u2);
			}
		}
		return result;
	}
	
	public int gradoSimile(User u) {
		int max=0;
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if(max<this.grafo.getEdgeWeight(e)) {
				max=(int) this.grafo.getEdgeWeight(e);
			}
		}
		return max;
	}
	
	public Set<User> getVertici(){
		return this.grafo.vertexSet();
	}
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
