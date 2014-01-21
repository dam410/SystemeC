
import java.rmi.RemoteException;
import java.util.ArrayList;




public class ServerObject {
	private boolean lr = false;
	private boolean lw = false;
	private Client_itf ecrivain;
	private ArrayList<Client_itf> lecteurs = new ArrayList<Client_itf>();
	private Integer id;
	private Object obj;
	
	public ServerObject (Integer i, Object o){
		id = i;
		obj = o;
	}
	
	
	public synchronized Object lock_read(Client_itf cl) throws RemoteException{
		if (lw) {
			obj= ecrivain.reduce_lock(id);
			lecteurs.add(ecrivain);
			lw = false;
		}
		if (cl != null){
		lr = true;
		lecteurs.add(cl);
		}
		return obj;
	}
	
	public synchronized Object lock_write(Client_itf cl) throws RemoteException{
		if (lw){
			//System.out.println("Le Client ecrivain est " + cl.hashCode());
			obj= ecrivain.invalidate_writer(id);
		}
		else{
			/*System.out.println("Le Client qui tente le lock_write est " + cl.hashCode() + "; "
					+ "la taille de la listee est" + lecteurs.size());
			*/
			for (int i =0; i<lecteurs.size(); i++){
				//System.out.println("Le client a la position " + lecteurs.indexOf(cl) + "\n");
				if(!lecteurs.get(i).equals(cl)) {
					//System.out.println("Le Client lecteur suivant " + lecteurs.get(i).hashCode());
					lecteurs.get(i).invalidate_reader(id);
				}
				
			}
			lecteurs.clear();
		}
		ecrivain = cl;
		lw = true;
		lr = false;
		//System.out.println("Le Client " + cl.hashCode() + "finit le lock write au niveau server");
		return obj;
	}
}