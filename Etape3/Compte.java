
public class Compte implements java.io.Serializable {
	
	private int solde;
	private Banque_itf banque;
	
	public Compte() {
		solde = 0;
	}

	@read
	public int getSolde() {
		return solde;
	}

	@write
	public void setBanque(Banque_itf b) {
		banque = b;	
	}
	
	@write
	public void ajoutSolde(int somme) {
		solde += somme;
		// On met à jour la banque
	}

}
