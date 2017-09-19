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
     * function that returns a description of the last question associated with the game.
     * @param username is a name of player associated with the game.
     * @return a description of the last question associated to the game.
     * @pre. the description is associated with a question in db.
     * @post. returns a description of the last question associated with the game.
     */
	public static String initGame(String username){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia","root", "root");
	    Game game = new Game();
	    List<Game> games  = Game.where("user ='"+username+"'");
	    Question q = new Question();
	    String description = "";
	    if(games.size()!=0){
	      	game = Game.findFirst("user = ?",username);
	      	description = game.getString("description");
	      	Question.getQuestionByDesc(description);
	    }else{
	      	game.set("user",username);
	      	q = Question.getQuestion();
	      	description = q.getString("description"); 
	    	game.set("description",description);
	   		game.saveIt();
	   	}
	    Base.close();
	    return description;
	}
}//End Class Game