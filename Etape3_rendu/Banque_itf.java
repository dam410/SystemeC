import java.util.ArrayList;


public interface Banque_itf extends SharedObject_itf{


	public void majCapital();
	

	public int getCapital();
	

	public void ajoutCompte(Compte_itf c);
	


	
}
