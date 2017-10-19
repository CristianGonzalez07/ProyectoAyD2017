package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

public class Notification extends Model{
	static{
		validatePresenceOf("origin").message("Please, provide your origin user");
		validatePresenceOf("destination").message("Please, provide your destination user");
		validatePresenceOf("content").message("Please, provide your content");
		validatePresenceOf("viewed").message("Please, provide your status of viewed");
		validatePresenceOf("accepted").message("Please, provide your status of accepted");
	}

	 /** 
     * function that creates a user notification
     * @param origin user origin of the notification 
     * @param destination user notification destination 
     * @param content content of notification
     * @return true if the notification was created correctly, but false
     * @pre. origin != null, destination != null, content != null.
     * @post. true is returned if the notification was created correctly otherwise
     * false.
     */
	public static boolean createNotification(String origin , String destination, String content){

		Notification n = new Notification();
		n.set("origin",origin);
		n.set("destination",destination);
		n.set("content",content);
		n.set("viewed",false);
		n.set("accepted",false);
		Boolean result = n.save();
		return result;
	}
}
