package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;

public class Game extends Model {
	
	static{
    	validatePresenceOf("user").message("Please, provide your username");
  		validatePresenceOf("description").message("Please, provide your password");
	}
}//End Class Game