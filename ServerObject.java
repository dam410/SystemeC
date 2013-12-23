import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;




public class ServerObject {
	private boolean lr = false;
	private boolean lw = false;
	private Client ecrivain;
	private ArrayList<Client> lecteurs = new ArrayList<Client>();
	private Integer id;
	private Object obj;
	
	public ServerObject (Integer i, Object o){
		id = i;
		obj = o;
	}
	
	
	public Object lock_read(Client cl) throws RemoteException{
		if (lw) {
			obj= ecrivain.reduce_lock(id);
			lw = false;
		}
		lr = true;
		lecteurs.add(cl);
		return obj;
	}
	
	public Object lock_write(Client cl) throws RemoteException{
		if (lw){
			obj= ecrivain.invalidate_writer(id);
		}
		else{
			for (int i =0; i<lecteurs.size(); i++){
				lecteurs.get(i).invalidate_reader(id);
			}
			lr = false;
		}
		return obj;
	}
}