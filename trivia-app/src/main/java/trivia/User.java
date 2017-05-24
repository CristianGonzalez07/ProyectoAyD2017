package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;

public class User extends Model {
	
	static{
    	validatePresenceOf("username").message("Please, provide your username");
  		validatePresenceOf("password").message("Please, provide your password");
	}

	//crea una cuenta de usuario con un nombre de usuario unico
	public static void createAccount(String username,String password){			
  		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		User p = User.createIt("username",username,"password",password);
		Base.close();
	}//End createAccount

	//verifica el inicio de sesion	
	public static Boolean login(String username,String password,Boolean asAdmin){	
 		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		User p = new User();
		p = User.findFirst("username = "+username);
		String p1 = p.getString("username");
		String p2 = p.getString("password");
		String p3 = p.getString("permissions");
		Boolean res;
		if(asAdmin){
			res = ((p1==username)&&(p2==password)&&(p3=="YES"));
		}else{
			res = ((p1==username)&&(p2==password));
		}
		Base.close();
		return res;
  	}//End Login

}//End Class User