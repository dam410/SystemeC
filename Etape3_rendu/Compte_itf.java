import java.io.Serializable;


public interface Compte_itf extends SharedObject_itf {
	



	public int getSolde();

	public void setBanque(Banque_itf b);
	
	public void ajoutSolde(int somme);

}
