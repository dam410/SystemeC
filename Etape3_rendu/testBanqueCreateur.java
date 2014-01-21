
public class testBanqueCreateur {

	public static void main(String[] args) {
		
		Client.init();
		
		// on créer la banque et deux comptes
		Banque_itf b = (Banque_itf)Client.create(new Banque());
		Client.register("MaBanque", b);
		Compte_itf c1 = (Compte_itf) Client.create(new Compte());
		Client.register("C1", c1);
		Compte_itf c2 = (Compte_itf) Client.create(new Compte());
		Client.register("C2", c2);
		
		//Affiche le solde avant ajout compte
		System.out.println("Affichage du capital avant l'ajout des comptes : " + b.getCapital());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//On ajoute les comptes
		System.out.println("classe de  obj " + ((SharedObject)b).getObj().getClass().getName());
		b.ajoutCompte((Compte_itf) c1);
		c1.setBanque(b);
		// On ajoute de l'argent sur c1
		c1.ajoutSolde(100);
		b.majCapital();
		System.out.println("Affichage du capital après ajout de 100 sur c1 : " + b.getCapital());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b.ajoutCompte(c2);
		c2.setBanque(b);
		c2.ajoutSolde(1000);
		b.majCapital();
		System.out.println("Affichage du capital après ajout de 1000 sur c2 : " + b.getCapital());
		
	}

}
