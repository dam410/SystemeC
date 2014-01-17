package Etape1;
import java.util.ArrayList;


public class ClientStress extends Thread {

	SharedObject s;
	ArrayList<Integer> commande = new ArrayList<Integer>();
	int numero_commande = 0;
	int id;
	int nb_Ecriture = 0;
	
	public ClientStress(int num) {
		id = num;
	}
	
	
	@Override
	public void run() {
		Client.init();
		s = Client.lookup("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
		}
		while(true) {
			executeCommande();
		}
		
	}
	
	public synchronized void lock_read() {
		commande.add(1);

	}
	public synchronized void lock_write() {
		commande.add(2);

	}
	public synchronized void unlock() {
		commande.add(3);

	}
	public synchronized void executeCommande() {
		if (commande.size() > numero_commande) {
			
			if(commande.get(numero_commande)==1){
				s.lock_read();
				System.out.println("Je lis : " + ((Sentence) s.obj).read());
				}
			else if (commande.get(numero_commande) ==2){
				s.lock_write();
				((Sentence) s.obj).write("Je suis "+id+", ça fait "+nb_Ecriture+" que j'écris.");
			}
			else if (commande.get(numero_commande) == 3)
				s.unlock();
			numero_commande++;
		}
	}

}
