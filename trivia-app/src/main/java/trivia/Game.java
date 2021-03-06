package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.ArrayList;
import trivia.Question;
import trivia.User;
import java.util.Date;
import java.sql.Timestamp;

public class Game extends Model {

	static{
    	validatePresenceOf("typeOfGame").message("Please, provide your typeOfGame");
    	validatePresenceOf("player1").message("Please, provide your player 1");
    	validatePresenceOf("status").message("Please, provide your status");
	}

	/**
     * function that saves a question associated with the game.
     * @param is the question to keep.
     * @param is the game where we will save the question
     * @pre. question != "",id >0;
     * @post. saves a question associated with the game.
    */
	public static void setQuestion(String question,int idGame){
		Game game = findFirst("id = ?", idGame);
		game.set("question",question);
		game.saveIt();
	}

	/**
     * function that creates a game for 1 player
     * @param player is player 1 of the game
     * @pre. player != null.
     * @post.  creates a game for 1 player
     */
	public static void createGame1Player(String player){
		Game game = new Game();
		game.set("typeOfGame","1PLAYER");
		game.set("player1",player);
		game.set("status","INPROGRESS");
		game.saveIt();
		Long id = (Long)game.get("id");
		User user = User.findFirst("username = ?",player);
		user.set("currentGame",id);
		user.saveIt();
	}

	/**
     * function that creates a game for 2 players
     * @param  player is player 1 of the game
     * @param  player is player 2 of the game
     * @pre. player1 != null, player2 != null.
     * @post. creates a game for 2 players
     */
	public static void createGame2Player(String player1,String player2){
		Game game = new Game();
		game.set("typeOfGame","2PLAYER");
		game.set("player1",player1);
		game.set("player2",player2);
		game.set("status","WAITING");
		game.saveIt();
	}

	/**
     * function that starts a game for 2 players
     * @param idGame is the id associated with the corresponding game to start.
     * @pre. idGame >=1;
     * @post. starts a game for 2 players
     */
	public static void startGame2Player(int idGame){
		Game game = findFirst("id = ?", idGame);
		game.set("status","INPROGRESS");
		Date date = new Date();
		Timestamp current = new Timestamp(date.getTime());
		game.set("initiated",current);
		game.saveIt();
	}


	/**
     * the game controls the completion of a game
     * @param idGame is the id associated with the corresponding game to start.
     * @pre. idGame >=1;
     * @post. returns true if the game has completed all its rounds
    */
	public static boolean endGame (int idGame){
		Game game = findFirst("id = ?",idGame);
		boolean resp = false;
		String player = (String)game.get("typeOfGame");
		int round = (int)game.get("moves");
		if(player.equals("1PLAYER")){
			if (round == 10){
				resp = true;
				game.set("status","TERMINATED");
				game.saveIt();
			}
		}
		if(player.equals("2PLAYER")){
			if (round == 20){
				resp = true;
				game.set("status","TERMINATED");
				game.saveIt();
			}
		}

		return resp;
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
            	String p1 = currentGame.getString("player1");
            	String p2 = currentGame.getString("player2");
                currentGame.delete();
               	Invitation.deleteInvitation(p1,p2);
            }
        }
    }

    /**
     * function that limits the duration of the game
     * @pre. true;
     * @post. if the game exceeds the maximum time of a match, it is given as finished
    **/
    public static void checkProgresGame(){
    	List<Game> games = Game.where("status = INPROGRESS");
    	Date date = new Date();
    	Timestamp current = new Timestamp(date.getTime());
    	Timestamp startDate = new Timestamp(0,0,0,0,0,0,0);
    	Game currentGame = new Game();
    	for(int i=0; i<games.size(); i++){
    		currentGame = games.get(i);
    		startDate = currentGame.getTimestamp("initiated");
    		int days1 = current.getDate();
    		current.setDate(days1-2);
    		int valor = current.compareTo(startDate);
    		if (valor > 0){
    			GameHandling.totalScoreForPlayer((int)currentGame.get("id"));
    			currentGame.delete();
    		}
    	}
    }

    /**
     *function that modifies the current game round.
     * @param idGame is a ID associated with the game.
     * @pre. id > 0.
     * @post. modifies the current game round.
    */
    public static void updateMoves(int idGame){
    	Game game = findFirst("id = ?",idGame);
		int round = (int)game.get("moves");
		game.set("moves",round+1);
		game.saveIt();
    }

	/**
     * function that modifies a user's current score.
     * @param idGame is a ID associated with the game.
     * @pre. id > 0.
     * @post. modifies user's current score.
  */
	public static void currentScore(int idGame){
		Game game = findFirst("id = ?",idGame);
		int score = 0;
		int round = (int)game.get("moves");
		String gameMode = game.getString("typeOfGame");
		if(gameMode.equals("1PLAYER")){
			score = (int)game.get("scorePlayer1");
			game.set("scorePlayer1", score+1);
		} else {
			if (round%2!=0){
				score = (int)game.get("scorePlayer1");
				game.set("scorePlayer1", score+1);
			} else {
				score = (int)game.get("scorePlayer2");
				game.set("scorePlayer2", score+1);
			}
		}
		updateMoves(idGame);
		game.saveIt();
	}



	/**
     * function that's returns true if the limit of games
     * per player is not exceeded.
     * @param username is a name of player
     * @pre. username <> []
     * @post. return true if the limit of games
     * per player is not exceeded.
     * @return true if if the limit of games per
     * player is not exceeded.
    */
	public static boolean limitGames(String username){
		List<Game> games = Game.where(("(player1 = '" + username + "' OR player2 = '" + username+"') and status='INPROGRESS'"));
		boolean res = false;
		if (games.size()<= 4){
			res=true;
		}
		return res;
	}

	/**
     * function that verifies the correctness of a response given by a player.
     * @param username is a name of player associated with the game.
     * @return true if the player answer is correct.
     * @pre. id > 0, playerAnswer != "";
     * @post. returns true if the player answer is correct.
    */
	public static boolean answer(int idGame,String playerAnswer){
  		Game game = findFirst("id = ?",idGame);
  		String description = game.getString("question");
  		Question q = Question.getQuestionByDesc(description);
    	String correctAnswer = q.getString("option1");
    	return playerAnswer.equals(correctAnswer);
	}
	/**
     * function that's returns the id that refers to the game in which the 2 given users participate
     * @param user1 is a name of player 1
     * @param user2 is a name of player 2
     * @pre. user1 <> [] and user2 <> []
     * @post. return the id that refers to the game in which the 2 given users participate
     * @return the id that refers to the game in which the 2 given users participate
    */
	public static int findIdGame(String user1,String user2){
		List<Game> games_aux = Game.where(("player1 = '" + user1 + "' AND player2 = '" + user2+"'"));
		List<Game> games_aux2 = Game.where(("player1 = '" + user2 + "' AND player2 = '" + user1+"'"));
	    List<Game> games = new ArrayList<Game>();
	    	String status = "";
	    	for (int i = 0;i<games_aux.size();i++) {
	    		status = games_aux.get(i).getString("status");
	        	if(status.equals("WAITING")){
	          		games.add(games_aux.get(i));
	        	}
	       	}

	       	for (int i = 0;i<games_aux2.size();i++) {
	    		status = games_aux2.get(i).getString("status");
	        	if(status.equals("WAITING")){
	          		games.add(games_aux2.get(i));
	        	}
	       	}
	       
	       	return (int)games.get(0).get("id");
	}

	/**
	 * function that returns the name of the player 1.
	 *@param id is the id of a game
	 *@pre. id>0
	 *@post. returns the name of the player 1.
	 *@return  the name of the player 1.
	*/
	public static String player_1(int id){
		Game game  =  findFirst("id = ?",id);
		return game.getString("player1");
	}

	/**
	 * function that returns the name of the player 2.
	 *@param id is the id of a game
	 *@pre. id>0
	 *@post. returns the name of the player 2.
	 *@return  the name of the player 2.
	*/
	public static String player_2(int id){
		Game game  =  findFirst("id = ?",id);
		return game.getString("player2");
	}

	public static List<String> games(String username){
		List<Game> games_aux = Game.where(("player1 = '" + username + "' OR player2 = '" + username+"'"));
		List<Game> games = new ArrayList<Game>();
		String status = "";
		for (int i = 0;i<games_aux.size();i++) {
			status = games_aux.get(i).getString("status");
			if(status.equals("INPROGRESS")){
				games.add(games_aux.get(i));
			}
		}
		List<String> list = new ArrayList<String>();
		int id = 0;
		for (int i = 0;i<games.size();i++) {
			id = (int)games.get(i).get("id");
			list.add(Integer.toString(id));
		}
		return list;
	}

	public static int actualMoves(int id){
		Game game  =  findFirst("id = ?",id);
		return (int)game.get("moves");
	}

	public static int getScoreP1(int id){
		Game game  =  findFirst("id = ?",id);
		return (int)game.get("scorePlayer1");
	}

	public static int getScoreP2(int id){
		Game game  =  findFirst("id = ?",id);
		return (int)game.get("scorePlayer2");
	}

	 /**
   *function that returns the current game type for the given game id.
   *@param idGame is the id of is the id of the game that you want to
   * know your type of game.
   *@return the current game type.
   *@pre. id>0.
   *@post. returns the current game type if exists,otherwise returns null.
  */
    public static String getCurrentGameType(int idGame){
      Game game = Game.findFirst("id = ?", idGame);
      return game.getString("typeOfGame");
    }
}
