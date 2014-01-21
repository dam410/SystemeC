

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class GenerateurStub {
	
	public static SharedObject creation(Object o){
		Class c = o.getClass();
		ecriture(c);
		compilateur(c.getSimpleName()+"_stub.java");
		//System.out.println("J'ai compilé");
		Class classeStub = null;
		try {
			classeStub = Class.forName(o.getClass().getSimpleName()+"_stub");
			//System.out.println("Classe obtenu stub");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		if(getStub(classeStub, o) == null)
			System.out.println("Si l'objet est null !!!");
		*/
		SharedObject res = getStub(classeStub, o);
		res.setObj(o);
		res.lock = Lock.NL;
		return res;
	}
	
	
	
	
	public static void ecriture(Class c){
		File f = new File(c.getSimpleName()+"_stub.java");
		if(f.exists()) {
			f.delete();
		}
		//System.out.println("Le fichier a été crée");
		try {
			
			FileWriter fw = new FileWriter(f);
			// Ecriture de l'entête
			fw.write("public class " + c.getSimpleName() + "_stub extends SharedObject implements " + c.getSimpleName() + "_itf, java.io.Serializable {\n");
			// Ecriture des attributs de la classe
			// AJout de l'attribut serialisation
			fw.write("private static final long serialVersionUID = 1L;\n");
			//Ajout des méthodes
			Method[] methodes = c.getDeclaredMethods();
			for(int i = 0;i<methodes.length;i++) {
				//Création de l'entête
				Method m = methodes[i];
				String s = getModifiersFromInt(m.getModifiers());
				Class ret = m.getReturnType();
				s+=ret.getName()+" "+m.getName();
				Class parametre[] = m.getParameterTypes();
				// Parcours de la liste des parametres
				s+="(";
				for(int j = 0;j<parametre.length;j++) {
					if(j != 0) {
						s+=",";
					}
					s+=parametre[j].getName()+" "+"param"+j;
				}
				s+=") {\n";
				//On écrit le champ de la méthode
				boolean isShared = false;
				//On vérifie les annotations
				//System.out.println("On regarde si l'annotation existe sur la méthode.");
				if(m.isAnnotationPresent(read.class)) {
					//System.out.println("Annotation trouvé");
					s+="lock_read();\n";
					isShared = true;
				}
				else if(m.isAnnotationPresent(write.class)) {
					s+="lock_write();\n";
					isShared = true;
				}
				//On Insere le obj.methode();
				
				
				// SI il y a un retour
				if(m.getReturnType() != void.class) {
					s+=m.getReturnType().getName()+" retour = ";
				}
				s+="(("+c.getSimpleName()+") this.obj)."+m.getName()+"(";
				//Insertions des parametres
				for(int j = 0;j<parametre.length;j++) {
					if(j != 0) {
						s+=",";
					}
					s+="param"+j;
				}
				s+=");\n";
				
				// On verifie si on doit unlock
				if(isShared) {
					s+="unlock();\n";
				}
				//Si il y a un retour
				if(m.getReturnType() != void.class) {
					s+="return retour;\n";
				}
				
				//On ferme notre méthode
				s+="}\n";
				// On ajoute notre méthode
				fw.write(s);
			}
			// Fermeture de la classe
			fw.write("}");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getModifiersFromInt(int mod) {
		String result;
		switch(mod) {
		case 1 :
			result = "public ";
			break;
		case 2 :
			result = "private ";
			break;
		case 4 :
			result = "protected ";
			break;
		default :
			result = null;
		}
		return result;
	}
	
	public static void compilateur (String name) {
		File fichier = new File(name);	
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjects(fichier)).call();

	}
	
	public static SharedObject getStub(Class c, Object o) {
		SharedObject stub = null;
		try {
			stub = (SharedObject) c.newInstance();
			try {
				c.getField("obj").set(stub, o);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (SharedObject) stub;
	}
	
	public static Object getStub(Object o) {
		return null;
	}
}
