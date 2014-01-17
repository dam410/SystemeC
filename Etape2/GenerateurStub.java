package Etape2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GenerateurStub {
	
	public static void ecriture(Class c){
		File f = new File("./"+c.getName()+"_stub.java");
		try {
			FileWriter fw = new FileWriter(f);
			// Ecriture de l'entête
			fw.write("public class " + c.getName() + "_stub extends SharedObject implements " + c.getName() + "_itf, java.io.Serializable {\n");
			// Ecriture des attributs de la classe
			/*Field[] attributs = c.getFields();
			for(int i = 0;i<attributs.length;i++) {
				//String s = getModifiersFromInt(attributs[i].getModifiers());
				fw.write(attributs[i].toGenericString()+";\n");
			}
			*/
			//Ajout de l'attribut Objet (classe métier)
			fw.write("private " + c.getName() + " obj;\n");
			//Ajout des méthodes
			Method[] methodes = c.getDeclaredMethods();
			for(int i = 0;i<methodes.length;i++) {
				String s = (methodes[i].toGenericString() + "{\n");
				//+ "obj." + methodes[i].getName();
				fw.write(s);
				if (!methodes[i].getReturnType().equals(void.class)) {
					fw.write("return ");
				}
				fw.write("obj." + methodes[i].getName() + "(");
				// Obtention de la liste des paramètres
				methodes[i].getPa
			}
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
	
	public static Object getStub(Object o) {
		return null;
	}
}
