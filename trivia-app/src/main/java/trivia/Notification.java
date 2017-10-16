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

	public static boolean createNotification(string origin , string destination, string content){

		Notification n = new Notification();
		n.set("origin",origin);
		n.set("destination",destination);
		n.set("content",content);
		n.set("viewed",false);
		n.set("accepted",false);
		Boolean respuesta = n.save();
		return respuesta;
	}
}
