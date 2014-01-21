import java.util.ArrayList;


public class Banque implements java.io.Serializable {
	
	private int capital;
	private ArrayList<Compte_itf> comptes;
	
	public Banque() {
		capital = 0;
		comptes = new ArrayList<Compte_itf>();
	}
	
	@write
	public void majCapital() {
		int somme = 0;
		for(int i = 0;i<this.comptes.size();i++) {
			somme += comptes.get(i).getSolde();
		}
		capital = somme;
	}
	
	@read
	public int getCapital() {
		return capital;
	}
	
	@write
	public void ajoutCompte(Compte_itf c) {
		comptes.add(c);
		majCapital();
	}
	

}
