import java.rmi.*;
import java.util.Hashtable;


public class Server implements Server_itf {
	private Hashtable<String, Integer> tab = new Hashtable<String, Integer>();
	private Hashtable<Integer, ServerObject> objets = new Hashtable<Integer, ServerObject>();

	@Override
	public int lookup(String name) throws RemoteException {
		// TODO Auto-generated method stub
		Integer i = tab.get(name);
		return objets.get(i);
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		// TODO Auto-generated method stub
		tab.put(name, id);
	}

	@Override
	public int create(Object o) throws RemoteException {
		// TODO Auto-generated method stub
		return tab.size();
	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
