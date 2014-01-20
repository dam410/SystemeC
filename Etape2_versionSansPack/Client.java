
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.Hashtable;
import java.net.*;


public class Client extends UnicastRemoteObject implements Client_itf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Server_itf serveur;
	private static Client moi;
	private static Hashtable<Integer, SharedObject> objets = new Hashtable<Integer, SharedObject>();
	
	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		//Lookup RMI
		try {
			moi = new Client();
			serveur = (Server_itf) Naming.lookup("//localhost:1098/ServeurNom");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public synchronized static SharedObject lookup(String name) {
		System.out.println("Lookup Client");
		//On appelle le serveur, pour trouver l'Id de l'objet demand�
		Integer id = null;
		SharedObject so = null;
		try {
			id = serveur.lookup(name);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// On cr�er un SharedObject, l'objet n'est pas encore coh�rent.
		// Dans le cas ou l'objet existe bien.
		if (id != -1) {
			Object o = null;
				
				try {
					// Il faut faire un appel direct au serveur sans passer par la table non initialisée
					o = serveur.lock_read(id,null);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// On a récupéré un version de l'objet, on peut créer le stub
				
				so = GenerateurStub.creation(o);
				so.setId(id);
				so.setObj(o);
				objets.put(id, so);
			}
			
		return so;
		
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		if(so == null)
			System.out.println("so est null");
		Integer id = ((SharedObject) so).getId();
		try {
			System.out.println("Je register au serveur avec " + name + " et id = " + id);
			serveur.register(name,id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		System.out.println("Client create");
		Integer id = null;
		try {
			id = serveur.create(o);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SharedObject stub = GenerateurStub.creation(o);
		stub.setId(id);
		stub.lock = Lock.NL;
		//TEST DE LECTURE
		Sentence_itf s = (Sentence_itf) stub;
		((SharedObject)s).setObj(o);
		if(((SharedObject)s).getObj() == null) {
			System.out.println("On ne peut pas set l'objet");
		}
		if (stub.obj == null)
			System.out.println("L'objet obj est null après la génération du _stub");
		objets.put(id,stub);
		return(stub);
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	
	// Ces m�thodes seront appel� par Les SharedObjects
	
	
	// request a read lock from the server
	public static Object lock_read(int id) throws RemoteException{
		// Il faut r�gler le probl�me de this static !
		System.out.println("je fais un lock_read");
		return serveur.lock_read(id, moi);
	}

	// request a write lock from the server
	public static Object lock_write (int id) throws RemoteException{
		// Il faut r�gler le probl�me de this static !
		System.out.println("J'appelle un lock write sur le serveur du client");
		Object o = serveur.lock_write(id, moi);
		System.out.println("L'objet est retourné après un lock_write vers le client");
		if (o != null)
			System.out.println("L'objet n'est pas null");
		return o;
	}

	
	// Ces M�thodes seront appel�s par les ServerObjects (par l'interm�diaire du Serveur de Nom)
	
	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		// Il faut les refaire passer vers les SharedObject, 
		//il est donc n�cessaire de cr�er une table pour retrouver les SharedObject par leurs ID
		return objets.get(id).reduce_lock();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		objets.get(id).invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return objets.get(id).invalidate_writer();
	}
}
