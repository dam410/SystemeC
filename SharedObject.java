import java.io.*;
import java.rmi.RemoteException;

public class SharedObject implements Serializable, SharedObject_itf {
	
	public Object obj;
	private Integer id;
	private boolean lr = false;
	private boolean lw = false;
	
	public SharedObject(Object ob, Integer iden){
		obj=ob;
		id=iden;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		// Il faut récupérer l'objet auprès du server et donc du ServerObject
		try {
			obj = Client.lock_read(id);
			lr = true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// invoked by the user program on the client node
	public void lock_write() {
		// Il faut récupérer l'objet auprès du sever et donc du ServerObect
		try {
			obj = Client.lock_write(id);
			lw = true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		lr = false;
		lw = false;
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		lw = false;
		lr = true;
		return obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		lr = false;
		lw = false;
	}

	public synchronized Object invalidate_writer() {
		lw = false;
		lr = false;
		return obj;
	}

	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
