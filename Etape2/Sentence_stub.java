package Etape2;

public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
private Sentence obj;
private static final long serialVersionUID = 1L;

public void write(java.lang.String param0) {
lock_write();
obj.write(param0);
unlock();
}

public java.lang.String read() {
lock_read();
java.lang.String retour = obj.read();
unlock();
return retour;
}
}