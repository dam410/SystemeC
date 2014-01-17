package Etape2;

public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
	
	public void write(String text) {
		lock_write();
		Sentence s = (Sentence)obj;
		s.write(text);
		unlock();
	}
	public String read() {
		Sentence s = (Sentence)obj;
		return s.read();	
	}
	
}