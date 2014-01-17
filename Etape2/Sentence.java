package Etape2;
public class Sentence implements java.io.Serializable {
	
	String 		data;
	
	public Sentence() {
		data = new String("");
	}
	
	@write
	public void write(String text) {
		data = text;
	}
	
	@read
	public String read() {
		return data;	
	}


	
}