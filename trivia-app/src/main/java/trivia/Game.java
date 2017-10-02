package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import trivia.Question;

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
     * @pre. username <> []
     * @post. returns a description of the last question associated with the game.
     */
	public static String getQuestion(String username){
	    Game game = new Game();
	    List<Game> games  = Game.where("user ='"+username+"'");
	    Question q = new Question();
	    String description = "";
	    game = Game.findFirst("user = ?",username);
	    description = game.getString("description");
	   	return description;
	}

	/** 
     * function that created and returns a description of the last question associated with the game.
     * @param username is a name of player associated with the game.
     * @return a description of the last question associated to the game.
     * @pre. username <> []
     * @post. returns a description of the last question associated with the game.
     */
	public static String newQuestion(String username){
		Game game = new Game();
	    List<Game> games  = Game.where("user ='"+username+"'");
	    Question q = new Question();
	    String description = "";
	   	if(games.size() != 0){
	   		game = Game.findFirst("user = ?",username);
	    }else{
	    	game.set("user",username);	
	    }
	    q = Question.getQuestion();
	    description = q.getString("description"); 
	   	game.set("description",description);
	   	game.saveIt();
	   	return description;	
	}
    /** 
     * function that returns the answer associated with a question in db.
     * @param username is a name of player associated with the game.
     * @return the response associated with the question answered by the user.
     * @pre. username <> []
     * @post. returns the response associated with the question answered by the user.
     */
	public static String answer(String username){
  		User user = User.findFirst("username = ?",username);
 		String description = getQuestion(username);
  		Question q = Question.getQuestionByDesc(description);
    	return question.get("option1");
	}


	/** 
     * function that modifies a user's current score and then returns it
     * @param username is a name of player associated with the game.
     * @return user's current score
     * @pre. username <> []
     * @post. modifies and returns user's current score
     */
	public static int currentScore(String username){
		User user = User.findFirst("username = ?",username);
		int score = (int)user.get("score");
	  	user.set("score",(score+1));
	  	user.saveIt();
	  	return score+1;
	}
}
