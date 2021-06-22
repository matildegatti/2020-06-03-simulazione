package it.polito.tdp.PremierLeague.model;

public class Opponenti implements Comparable<Opponenti>{

	Player p;
	double peso;
	public Opponenti(Player p, double peso) {
		super();
		this.p = p;
		this.peso = peso;
	}
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return p.playerID + "  " +p.name+" | "+ (int)peso;
	}
	@Override
	public int compareTo(Opponenti arg0) {
		if(this.peso>arg0.peso)
			return -1;
		else
			return 1;
	}
	
}
