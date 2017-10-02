package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class User extends Model {
	
	static{
    	validatePresenceOf("username").message("Please, provide your username");
  		validatePresenceOf("password").message("Please, provide your password");
  		validateWith(new UniquenessValidator("username")).message("This username is already taken."); 		
	}

	/** 
     * Function that creates a user returns a number 
     * depending on whether or not the user is reserved.
     * @param username is the name of the user to consult.
     * @param password is the password of the user to control
     * @return A number from 1 to 3, 1 if reserved, 2 if already 
     * available and 3 if not.
     * @pre. username <> [] and password <> []
     * @post. a number from 1 to 3 that must return
     */
	public static int register(String username, String password){
			int cont = 0;
			List<User> users  = User.where("username ='"+username+"'");
			if (users.size() != 0){
				cont = 1;
			}else{
				User usuario = new User();
				usuario.set("username",username);
				usuario.set("password",password);
				Boolean res = usuario.save();
				if (res == true){
					cont = 3;
				}else{
					cont = 2;
				}
			}
			return cont;
	}

	//verifica el inicio de sesion	
	public static Boolean validateLogin(String username,String password,String asAdmin){	
		List<User> users  = User.where("username ='"+username+"' AND password ='"+password+"'");
		if(users.size() != 0){
			User p =  users.get(0);
			String p1 = p.getString("permissions");
			if(asAdmin != null){
				return (p1.equals("YES"));
			}else{
				return (p1.equals("NO"));
			}				
		}else{
			return false;
		}
  	}
}