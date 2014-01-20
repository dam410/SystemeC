
import java.io.*;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

public class SharedObject implements Serializable, SharedObject_itf {
	

	
	public Object obj;
	private Integer id;
	public Lock lock;
	
	//constructeurs
	
	public SharedObject() {
		lock = Lock.NL;
	}
	
	public SharedObject(Object ob, Integer iden){
		lock = Lock.NL;
		obj=ob;
		id=iden;
	}
	
	// invoked by the user program on the client node
	public synchronized void lock_read() {
		// Il faut r�cup�rer l'objet aupr�s du server et donc du ServerObject
		try {
			switch (lock) {
				case WLC : 
					lock = Lock.RLT_WLC;
					break;
				case RLC :
					lock = Lock.RLT;
					break;
				case NL :
					obj = Client.lock_read(id);
					lock = Lock.RLT;
					break;
				default :
					System.out.println("Erreur : On tente un lock_read mais le lock est invalide : " + lock);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
	}

	// invoked by the user program on the client node
	public synchronized void lock_write() {
		// Il faut r�cup�rer l'objet aupr�s du sever et donc du ServerObect
		try {
			switch (lock) {
				case WLC : 
					lock = Lock.WLT;
					break;
				case RLC :
					obj = Client.lock_write(id);
					lock = Lock.WLT;
					break;
				case NL :
					obj = Client.lock_write(id);
					lock = Lock.WLT;
					break;
				default :
					System.out.println("Erreur : On tente un lock_write mais le lock est invalide : " + lock);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		if(lock == null)
			System.out.println("Le lock est null lors de l'unlock");
		switch (lock) {
			case WLT : 
				lock = Lock.WLC;
				break;
			case RLT_WLC :
				lock = Lock.WLC;
				break;
			case RLT :
				lock = Lock.RLC;
				break;
			default :
				System.out.println("Erreur : On tente un unlock mais le lock est invalide : " + lock);
		}
		notify();
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		switch (lock) {
			case WLT : 
				// On se doit d'attendre que l'User unlock l'objet
			try {
				while(lock != Lock.WLC && lock != Lock.RLT_WLC) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				if (lock == Lock.WLC) {
					lock = Lock.RLC;
				}
				else {
					lock = Lock.RLT;
				}
				break;
			case RLT_WLC :
				lock = Lock.RLT;
				break;
			case WLC :
				lock = Lock.RLC;
				break;
			default :
				System.out.println("Erreur : Le server tente un reduce_lock mais le lock est invalide : " + lock);
		}
		return obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		switch (lock) {
		case RLT :
			// On attend que le User unlock l'objet
			try {
				while(lock != Lock.RLC) {
					wait();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			lock = Lock.NL;
			break;
		case RLC :
			lock = Lock.NL;
			break;
		default :
			System.out.println("Erreur : Le server tente un invalidate_reader mais le lock est invalide : " + lock);
		}
	}

	public synchronized Object invalidate_writer() {
		switch (lock) {
		case RLT_WLC : 
			// On attend que le user unlock
			try {
				while(lock != Lock.WLC) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lock = Lock.NL;
			break;
		case WLT :
			try {
				wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			lock = Lock.NL;
			break;
		case WLC :
			lock = Lock.NL;
			break;
		default :
			System.out.println("Erreur : Le server tente un invalidate_writer mais le lock est invalide : " + lock);
		}
		return obj;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setObj(Object o) {
		obj = o;
	}
	public Object getObj() {
		return obj;
	}
	
	/*
	 * La troisième étape consiste simplement à faire en sorte que lorsqu'un SHareObject (référencé par un autre)
	 * est désérialisé sur un client qui ne connait pas l'objet. On assure sa cohérence en tant que
	 * ShareObject en l'enregistrant pour ce client.
	 * 
	 * 
	 * 
	 * 
	 */
	
}