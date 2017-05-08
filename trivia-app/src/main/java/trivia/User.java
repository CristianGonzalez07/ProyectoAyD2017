package trivia;

import org.javalite.activejdbc.Model;
import java.util.List;

public class User extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
  }

  public static Boolean exist(String s){

		List<User> users = where("username = '"+s+"'");
		if(users.size()==0){
			return false;
		}else{
			return true;
		}
	}

	public static Boolean isValid(String s){
		if(s.length()<5){
			return false;
		}else{
			return true;
		}
	}

}