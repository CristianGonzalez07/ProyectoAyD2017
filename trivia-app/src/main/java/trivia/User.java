package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.Scanner;

public class User extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
  }

  public static void createAccount(){			//crea una cuenta de usuario con un nombre de usuario unico
  	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		User p = new User();
		System.out.println("Ingrese Nombre de Usuario");
		String s1 = read();
		Boolean userExist = p.exist(s1);
		Boolean validUser = p.isValid(s1,4);

		if((!userExist)&&(validUser)){
			System.out.println("Ingrese Contraseña");
			String s2 = read();
			Boolean validPass = p.isValid(s2,5);
			if(validPass){
				p.set("username",s1);
	 			p.set("password",s2);
	 			p.saveIt();
			}
		}else{
			System.out.println("Error al crear la cuenta,verifique los datos ingresados");
		}	
		Base.close();
	}

  public static Boolean login(String permissions){	//funcion encargada de verificar el inico de sesion
 		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		User p = new User();
		System.out.println("Ingrese Nombre de Usuario");
		String s1 = read();
		Boolean userExist = p.exist(s1);
		Boolean validUser = p.isValid(s1,4);
		Boolean res = false; 

		if((userExist)&&(validUser)){
			System.out.println("Ingrese Contraseña");
			String s2 = read();
			Boolean validPass = p.isValid(s2,5);
			p = getUser(s1);
			String p1 = (String) p.get("password");
			String p2 = (String) p.get("permissions");

			if((validPass)&&(s2.equals(p1))&&(permissions.equals(p2))){
	 			res = true;
			}
		}else{
			System.out.println("Error al iniciar sesion,verifique los datos ingresados");
		}	
		Base.close();
		return res;
  }

  public static User getUser(String s){								//dado un username retorna todos los datos de un usuario
  	List<User> users = where("username = '"+s+"'");
  	return users.get(0);
  }

  private static String read(){										//retorna un string tomado desde el teclado
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}

  private static Boolean exist(String s){							//retorna true si un username esta en la base de datos
		List<User> users = where("username = '"+s+"'");
		if(users.size()==0){
			return false;
		}else{
			return true;
		}
	}

	private static Boolean isValid(String s,int n){		//retorna true si la cadena tiene como minimo la longitud dada
		if(s.length()<n){
			return false;
		}else{
			return true;
		}
	}
}