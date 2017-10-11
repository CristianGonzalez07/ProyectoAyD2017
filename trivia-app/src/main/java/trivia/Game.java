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
     * function that modifies a user's current score
     * @param username is a name of player associated with the game.
     * @pre. username <> []
     * @post. modifies user's current score
     */
	public static void currentScore(String p1,String p2){
		List<Game> games = Game.where("player1 = " + p1 + "AND player2 = " + p2);
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


	/**
	* function that modifies the total score of users
	* @param username of users associated with the game
	* @pre idGame <> []
	* @pos modifies user's total score
	*/
	public static void totalScoreForPlayer(int idGame){
		Game game = Game.findFirst("id = ?", idGame);
		String status = game.get("status");
		String typeOfGame = game.get("typeOfGame");
		if(status.equals("TERMINATED")){
			
			if(typeOfGame.equals("2PLAYER")){
				int scorePlayer1 = game.get("scorePlayer1");
				int scorePlayer2 = game.get("scorePlayer2");
				user user1 = game.get("player1");
				user user2 = game.get("player2");
				if(scorePlayer1>scorePlayer2)
					user1.set("score",scorePlayer1+15);
				else{
					if(scorePlayer1<scorePlayer2)
						user2.set("score",scorePlayer2+15);
					else{					
						user1.set("score",scorePlayer1);
						user2.set("score",scorePlayer2);
					}
				}
			}else{
				int scorePlayer1 = game.get("scorePlayer1");
				user user1 = game.get("player1");
				user1.set("score",scorePlayer1);
			}

		}


	}



}
