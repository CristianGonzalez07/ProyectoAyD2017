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
     * @param content content of invitation
     * @return true if the invitation was created correctly, but false
     * @pre. origin != null, destination != null, content != null.
     * @post. true is returned if the invitation was created correctly otherwise
     * false.
     */
	public static boolean createInvitation(String origin , String destination, String content){

		Invitation n = new Invitation();
		n.set("origin",origin);
		n.set("destination",destination);
		n.set("accepted",false);
		Boolean result = n.save();
		return result;
	}

	 /** 
     * 
     * @param content content of invitation
     * @return true if the invitation was created correctly, but false
     * @pre. origin != null, destination != null, content != null.
     * @post. true is returned if the invitation was created correctly otherwise
     * false.
     */
}
