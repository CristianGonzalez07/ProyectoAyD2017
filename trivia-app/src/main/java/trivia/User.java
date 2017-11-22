package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.ArrayList;
import org.javalite.activejdbc.validation.UniquenessValidator;
import trivia.Game;

public class User extends Model {

	static{
    	validatePresenceOf("username").message("Please, provide your username");
  		validatePresenceOf("password").message("Please, provide your password");
  		validateWith(new UniquenessValidator("username")).message("This username is already taken.");
	}

  /**
     * function that associates a current game id to the given user.
     * @param username is the name of the user associated with the id of the current game.
     * @param idGame it's the id to set
     * @pre. username != "",id >0;
     * @post. saves the id of current game associated with the username.
    */
  public static void setCurrentGame(String username,int id){
    User user = findFirst("username = ?", username);
    user.set("currentGame",id);
    user.saveIt();
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
	 *function that returns the username of a GameHandling.random()ly selected user.
	 *@param username is the name of the user who is looking for a rival
	 *@return the username of a GameHandling.random()ly selected user.
	 *@pre. true
	 *@post. returns the username of a GameHandling.random()ly selected user.
  */
  	public static String randomMatch(String username){
  		List<User> users  =  User.findAll();
     	String result = "";
  		int count = users.size();
  		User user = users.get(GameHandling.random(0,count));
  		String name = user.getString("username");
      String permissions = user.getString("permissions");
      System.out.println(name+"==="+permissions);
  		if(name.equals(username) || (permissions.equals("YES"))){
        	result = randomMatch(username);
      }else if(Invitation.existsInvitation(username,name)){
        	result = randomMatch(username);
      	}else{
          result = user.getString("username");
        }
  		return result;
  	}


  /**
   *function that returns the current game id for the given user.
   *@param user name is the user name of who wants to know the current game id.
   *@return the current game id.
   *@pre. username != null.
   *@post. returns the current game id if exists,otherwise returns null.
  */
    public static int getCurrentGameId(String username){
      User user =  findFirst("username = ?", username);
      int currentGame = (int)user.get("currentGame");
      return currentGame;
    }

    public static Boolean games(String username){
      List<Game> games_aux = Game.where(("player1 = '" + username + "' OR player2 = '" + username+"'"));
      List<Game> games = new ArrayList<Game>();
      String status = "";
      for (int i = 0;i<games_aux.size();i++) {
        status = games_aux.get(i).getString("status");
        if(status.equals("INPROGRESS")){
          games.add(games_aux.get(i));
        }
      }
      return games.size()!=0;
    }

		/**
	   *function that returns if a username exists.
	   *@param username username to whom you want to send an invitation.
	   *@return true if the user is in the system
	   *@pre. username != null.
	   *@post. returns the boolean if user exists,otherwise returns false.
	  */
		public static boolean userExists(String userOrig, String userDesti){
			List<User> users  = User.where("username ='"+userDesti+"'");
			if (users.size()!=0){
        if(!Invitation.existsInvitation(userOrig,userDesti)){
          Invitation.createInvitation(userOrig,userDesti);
          return true;
        }
			}
			return false;
		}
    
    public static int getScore(String username){
      User user =  findFirst("username = ?", username);
      return (int)user.get("score");
    }
    public static List<String> ranking(){
      List<User> users  = User.where("permissions = 'NO' order by score desc ");
      List<String> list = new ArrayList<String>();
      if(users.size()>0){
        String name="";
        int score = 0; 
        User user = null;
        for (int i=0;i<5;i++) {
          
          if(users.size()>i){
            user = users.get(i);
            name = user.getString("username");
            score = (int)user.get("score");
            list.add(name+": "+score);
          }else{
            list.add("----");
          }
        }
      }
      return list;
    }
}
