package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.ArrayList;

public class Invitation extends Model{
	static{
		validatePresenceOf("origin").message("Please, provide your origin user");
		validatePresenceOf("destination").message("Please, provide your destination user");
		validatePresenceOf("accepted").message("Please, provide your status of accepted");
	}

	 /** 
     * function that creates a user invitation
     * @param origin user origin of the invitation 
     * @param destination user invitation destination 
     * @pre. origin != null, destination != null
     * @post. creates a user invitation
     */
	public static boolean createInvitation(String origin , String destination){
          boolean res = false;
          long count = Invitation.count("origin = '"+origin+"' AND destination = '"+destination+"'");
          if (count == 0){
               Invitation n = new Invitation();
               n.set("origin",origin);
               n.set("destination",destination);
               n.set("accepted",false);
               res = n.save();
          }
		return res;
	}

	/** 
     * function that checks if the user has unaccepted invitations
     * @param username is the name of the user that you want to know
     * if you have invitations
     * @return true if the user has unaccepted invitations 
     * @pre. username != null.
     * @post. true is returned if the user has unaccepted invitations 
     */
     public static boolean newInvitations(String username){
          long count = Invitation.count("destination= ? ", username);
          return count!= 0;
     }


     /** 
     * function that returns the list of the first 3 user names
     * that invited the given user as a parameter to a game.
     * @param username is the name of the user who received the
     * invitation to a game.
     * @return the list of the first 3 user names
     * that invited the given user as a parameter to a game.
     * @pre. username != null.
     * @post. return the list of the first 3 user names
     * that invited the given user as a parameter to a game.
     */
     public static List<String> getInvitations(String username){
          List<String> listOfUsers = new ArrayList<String>();
          Invitation invitation = new Invitation();
          String origin = "";
          for(int i = 0;i<3;i++){
               invitation = findFirst("destination = ?",username);
               origin = invitation.getString("origin");
               listOfUsers.add(origin);
          }
          return listOfUsers;
     }
}
