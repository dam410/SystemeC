import java.io.*;

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
		lr = true;
	}

	// invoked by the user program on the client node
	public void lock_write() {
		lw = true;
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
	}

	public synchronized Object invalidate_writer() {
		lw = false;
		return obj;
	}
}
