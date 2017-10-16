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
}