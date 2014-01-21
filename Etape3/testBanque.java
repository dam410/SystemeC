import java.util.ArrayList;


public class testBanque {

	public static void main(String[] args) {
		
		Client.init();
		
		Banque_itf b = (Banque_itf)Client.lookup("MaBanque");
		
		
		// On attend
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Affichage de la copie du capital n°1 : " +b.getCapital());
		
		// On attend
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Affichage de la copie du capital n°2 : " +b.getCapital());
		
		// On attend
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Affichage de la copie du capital n°3 : " +b.getCapital());
		
		System.out.println("Taillde de objets de client " + Client.getObjets().size());
		

	}

}
