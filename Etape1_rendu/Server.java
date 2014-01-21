
import java.rmi.*;
import java.util.Hashtable;
import java.rmi.server.UnicastRemoteObject;


public class Server extends UnicastRemoteObject implements Server_itf {
	
	protected Server() throws RemoteException {
		super();
		tab = new Hashtable<String, Integer>();
		objets = new Hashtable<Integer, ServerObject>();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Integer> tab;
	private Hashtable<Integer, ServerObject> objets;

	@Override
	public int lookup(String name) throws RemoteException {
		System.out.println("Lookup Server");
		// On regarde si l'objet est d�j� enregistr� sur le serveur
		// Si ce n'est pas le cas on renvoit null !
		if (tab.get(name) == null) {
			return -1;
		}
		else 
			return (int) tab.get(name);
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		// On lie le nom de l'objet � son Id
		tab.put(name, id);
	}

	@Override
	public int create(Object o) throws RemoteException {
		System.out.println("Server create");
		// Cr�ation d'un ServerObjet et on le met dans la table.
		int id = tab.size();
		ServerObject so = new ServerObject(id,o);
		objets.put(id, so);
		return id;
	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		// On transmet l'info au serverObjet correspondant.
		// Il faudra faire attention aux exceptions NullPointer si jamais id n'est pas correct.
		Object reponse = objets.get(id).lock_read(client);
		return reponse;
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {
		Object reponse = objets.get(id).lock_write(client);
		return reponse;
	}

}
