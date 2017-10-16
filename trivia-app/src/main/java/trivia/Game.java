package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import trivia.Question;
import java.util.Date;
import java.sql.Timestamp;

public class Game extends Model {
		
	static{
    	validatePresenceOf("typeOfGame").message("Please, provide your typeOfGame");
    	validatePresenceOf("player1").message("Please, provide your player 1");
	}

	/** 
     * function that creates a game for 1 player 
     * @param player is player 1 of the game
     * @return true if the game was created correctly, otherwise false
     * @pre. player != null.
     * @post. true is returned if the game was created correctly, otherwise false.
     */
	public static boolean createGame1Player(String player){
		Game game = new Game();
		game.set("typeOfGame","1PLAYER");
		game.set("player1",player);
		game.set("status","INPROGRESS");
		boolean res = game.save();
		return res;
	}

	/** 
     * function that creates a game for 2 players 
     * @param  player is player 1 of the game 
     * @param  player is player 2 of the game
     * @return true if the game was created correctly,otherwise false
     * @pre. player1 != null, player2 != null.
     * @post. true is returned if the game was created correctly,otherwise false
     */
	public static boolean createGame2Player(String player1,String player2){
		Game game = new Game();
		game.set("typeOfGame","2PLAYER");
		game.set("player1",player1);
		game.set("player2",player2);
		game.set("status","WAITING");
		boolean res = game.save();
		return res;
	}

	/** 
     * function that starts a game for 2 players
     * @param idGame is the id associated with the corresponding game to start. 
     * @return true if the game can start correctly,otherwise false.
     * @pre. idGame >=1;
     * @post. true is returned if the game can start correctly,otherwise false.
     */
	public static boolean startGame2Player(int idGame){
		Game game = findFirst("id = ?", idGame);
		game.set("status","INPROGRESS");
		Date date = new Date();
		Timestamp current = new Timestamp(date.getTime());
		game.set("initiated",current);
		boolean res = game.save();
		return res;
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
		int round = (int)game.get("moves");
		int score = 0;
		if (round%2==0){
			score = (int)game.get("scorePlayer1");
			game.set("scorePlayer1", score+1);
		} else {
			score = (int)game.get("scorePlayer2");
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
		String status = game.getString("status");
		String typeOfGame = game.getString("typeOfGame");
		if(status.equals("TERMINATED")){
			String user1 = game.getString("player1");
			User p1 = User.findFirst("username = ?",user1);	
			if(typeOfGame.equals("2PLAYER")){
				int scorePlayer1 = (int)game.get("scorePlayer1");
				int scorePlayer2 = (int)game.get("scorePlayer2");
				String user2 = game.getString("player2");
				User p2 = User.findFirst("username = ?",user2);
				if(scorePlayer1>scorePlayer2)
					p1.set("score",scorePlayer1+15);
				else{
					if(scorePlayer1<scorePlayer2)
						p2.set("score",scorePlayer2+15);
					else{					
						p1.set("score",scorePlayer1);
						p2.set("score",scorePlayer2);
					}
				}
			}else{
				int scorePlayer1 = (int)game.get("scorePlayer1");
				p1.set("score",scorePlayer1);
			}

		}


	}



}
