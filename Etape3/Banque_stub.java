public class Banque_stub extends SharedObject implements Banque_itf, java.io.Serializable {
private static final long serialVersionUID = 1L;
public int getCapital() {
lock_read();
int retour = ((Banque) this.obj).getCapital();
unlock();
return retour;
}
public void ajoutCompte(Compte_itf param0) {
lock_write();
((Banque) this.obj).ajoutCompte(param0);
unlock();
}
public void majCapital() {
lock_write();
((Banque) this.obj).majCapital();
unlock();
}
}