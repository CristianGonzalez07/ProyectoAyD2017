package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

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
     * @return true if the invitation was created correctly, otherwise false
     * @pre. origin != null, destination != null
     * @post. true is returned if the invitation was created correctly otherwise
     * false.
     */
	public static boolean createInvitation(String origin , String destination){

		Invitation n = new Invitation();
		n.set("origin",origin);
		n.set("destination",destination);
		n.set("accepted",false);
		Boolean result = n.save();
		return result;
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
          List<String> listOfUsers = new List<String>();
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
