package Etape2;

public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
private static final long serialVersionUID = 1L;
public void write(java.lang.String param0) {
lock_write();
((Sentence) this.obj).write(param0);
unlock();
}
public java.lang.String read() {
lock_read();
java.lang.String retour = ((Sentence) this.obj).read();
unlock();
return retour;
}
}