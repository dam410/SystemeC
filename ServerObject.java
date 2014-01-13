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
	
	
	public Object lock_read(Client_itf cl) throws RemoteException{
		if (lw) {
			obj= ecrivain.reduce_lock(id);
			lecteurs.add(ecrivain);
			lw = false;
		}
		lr = true;
		lecteurs.add(cl);
		return obj;
	}
	
	public Object lock_write(Client_itf cl) throws RemoteException{
		if (lw){
			obj= ecrivain.invalidate_writer(id);
		}
		else{
			for (int i =0; i<lecteurs.size(); i++){
				lecteurs.get(i).invalidate_reader(id);
				lecteurs.remove(i);
			}
		}
		ecrivain = cl;
		lw = true;
		lr = false;
		return obj;
	}
}