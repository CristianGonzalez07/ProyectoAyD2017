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
    	return q.getString("option1");
	}


	/** 
     * function that modifies a user's current score
     * @param username is a name of player associated with the game.
     * @return user's current score
     * @pre. username <> []
     * @post. modifies user's current score
     */
	public static void currentScore(String p1,String p2){
		List<Game> games = Game.where("player1 = " + p1 + "AND player2 = " + p2)
		Game game = games.get(0);
		int round = game.get("moves");
		int score = 0;
		if (round%2==0){
			score = game.get("scorePlayer1");
			game.set("scorePlayer1", score+1);
		} else {
			score = game.get("scorePlayer2");
			game.set("scorePlayer2", score+1);
		}
		game.set("moves",round+1);
	}
}
