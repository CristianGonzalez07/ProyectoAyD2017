package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import trivia.Question;
import java.util.Date;
import java.sql.Timestamp;

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
     * function that limits the start time of a game.
     * @pre. true; 
     * @post. if a game exceeded its waiting time, it changes its
     * status to uninitiated.
    **/
    public static void checkWaitTime(){
        List<Game> games = Game.where("status = WAITING");
        Date date = new Date();
        Timestamp current = new Timestamp(date.getTime());
        Timestamp startDate = new Timestamp(0,0,0,0,0,0,0);
        int currentHour = current.getHours();
        int initialHour = 0;
        Game currentGame = new Game(); 
        for(int i=0;i<games.size();i++){
            currentGame = games.get(i);
            startDate = currentGame.getTimestamp("created_at");
            initialHour = startDate.getHours();
            if(initialHour+5<currentHour){
                currentGame.set("status","UNINITIATED");
            } 
        }
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
