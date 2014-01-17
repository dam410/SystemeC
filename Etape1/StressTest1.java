package Etape1;
import java.util.ArrayList;


public class StressTest1 {
	
	private static ArrayList<ClientStress> liste_client = new ArrayList<ClientStress>();

	public static void main(String[] args) {
		
		ClientStress cl_1 = new ClientStress(-1);
		cl_1.start();		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i<20;i++) {
			ClientStress cl = new ClientStress(i);
			liste_client.add(cl);
			cl.start();
			
		}
		for(int i = 0;i<10;i++) {
			liste_client.get(i).lock_write();
			liste_client.get(i+10).lock_read();
		}
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i<10;i++) {
			liste_client.get(i+10).unlock();
			liste_client.get(i).unlock();
		}
		

	}

}
