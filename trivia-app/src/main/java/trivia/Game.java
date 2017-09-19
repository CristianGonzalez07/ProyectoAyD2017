package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;

public class Game extends Model {
	//-----------------------------------------	
	static{
    	validatePresenceOf("user").message("Please, provide your username");
  		validatePresenceOf("description").message("Please, provide your password");
	}


	/** 
    	 * Comentar 
     	*/
	public static String answer(String username)(){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
  		User user = User.findFirst("username = ?",username);
 		String description =Game.initGame(username);
  		Question q = Question.getQuestionByDesc(description);
    		Base.close();
    		return Question.getAnswer(q);
	}


	/** 
     	 * Comentar 
     	*/
	public static void currentScore(String username){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		User user = User.findFirst("username = ?",username);
		int score = (int)user.get("score");
	  	user.set("score",(score+1));
	  	user.saveIt();
	  	Base.close();
	}
}//End Class Game
