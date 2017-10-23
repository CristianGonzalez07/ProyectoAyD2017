package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import org.javalite.activejdbc.validation.UniquenessValidator;
import java.util.Random;

public class User extends Model {
	
	static{
    	validatePresenceOf("username").message("Please, provide your username");
  		validatePresenceOf("password").message("Please, provide your password");
  		validateWith(new UniquenessValidator("username")).message("This username is already taken."); 		
	}
  
	/** 
     * function that returns a random number between the range given by the
     * parameters.
     * @param init is the smallest number in the range.
     * @param end is the largest number in the range.
     * @return a random number between the range given by the parameters.
     * @pre. 0 <= init <= end.
     * @post. a random number between the range given by the parameters is
     * returned.
     */
    public static int random(int init,int end) {
        Random  rnd = new Random();
        return (int)(rnd.nextDouble() * end + init);
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

    /** 
     * Function function that returns true if the user's password and
     * permissions are correct for system access.
     * @param username is the name of the user to consult.
     * @param password is the password of the user to control
     * @param asAdmin indicates the corresponding access permissions.
     * @return  true if the user's password and permissions are correct
     * for system access otherwise false.
     * @pre. username <> [] and password <> [] and 
     * (asAdmin="YES" or asAdmin = "NO")
     * @post. returns true if the user's password and
     * permissions are correct for system access.
     */
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

  	/**
	 *function that returns the username of a randomly selected user.
	 *@param username is the name of the user who is looking for a rival
	 *@return the username of a randomly selected user.
	 *@pre. true
	 *@post. returns the username of a randomly selected user.
  	 */
  	public static String randomMatch(String username){
  		List<User> users  =  User.findAll();
      String result = "";
  		int count = users.size();
  		User user = users.get(random(0,count));
  		String name = user.getString("username");
      String permissions = user.getString("permissions");
      System.out.println(name+"==="+permissions);
  		if(name.equals(username) || (permissions.equals("YES"))){
        result = randomMatch(username);
      }else{
        result = user.getString("username");
      }
  		return result;
  	}

}