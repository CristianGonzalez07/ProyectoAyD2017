package trivia;
import trivia.EchoWebSocket;
import trivia.Game;
import trivia.Question;
import java.util.List;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;
import java.util.Random;

public class GameHandling{


	/** 
     * function that returns a random number between the range given by the
     * parameters.
     * @param init is the smallest number in the range.
     * @param end is the largest number in the range.
     * @return a random number between the range given by the parameters.
     * @pre. 0 <= init <= end.
     * @post. a random number between the range given by the parameters is
     * returned.
     */
    public static int random(int init,int end) {
        Random  rnd = new Random();
        return (int)(rnd.nextDouble() * end + init);
    }

		public static void buildSiteForPlay(Session user,int id){
		String question = Question.getQuestion();
    	Game.setQuestion(question,id);
    	List<String> options = Question.mergeOptions(question);	
		try {
			if(user != null){
				if(!Game.endGame(id)){
					user.getRemote().sendString(String.valueOf(new JSONObject()
		                .put("play","yes")
		                .put("question",question)
		                .put("option1",options.get(0))
		                .put("option2",options.get(1))
		                .put("option3",options.get(2))
		                .put("option4",options.get(3))
		                .put("results","")
	            	));
				}else{
					String win = winner(id);
					int score1 = 0;
					int score2 = 0;
					if(win.equals(Game.player_1(id))){
						score1 = Game.getScoreP1(id);
						score2 = Game.getScoreP2(id);
					}else{
						score1 = Game.getScoreP2(id);
						score2 = Game.getScoreP1(id);
					}
					user.getRemote().sendString(String.valueOf(new JSONObject()
						.put("endGame","true")
						.put("winner",win)
						.put("typeOfGame",Game.getCurrentGameType(id))
						.put("scorep1",score1)
						.put("scorep2",score2)
						
					));
				} 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void buildSiteForWait(Session user,String player,int id){
		try {
			if(user != null){
				if(!Game.endGame(id)){
					user.getRemote().sendString(String.valueOf(new JSONObject()
						.put("play","no")
						.put("msgEspera","Espere...El turno de responder es de: "+player)
					));
				}else{
					String win = winner(id);
					int score1 = 0;
					int score2 = 0;
					if(win.equals(Game.player_1(id))){
						score1 = Game.getScoreP1(id);
						score2 = Game.getScoreP2(id);
					}else{
						score1 = Game.getScoreP2(id);
						score2 = Game.getScoreP1(id);
					}
					
					user.getRemote().sendString(String.valueOf(new JSONObject()
						.put("endGame","true")
						.put("winner",win)
						.put("typeOfGame",Game.getCurrentGameType(id))
						.put("scorep1",score1)
						.put("scorep2",score2)
						
					));
				}	
			}
		} catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void buildSiteFor2Players(Session user,int id,String currentUser){
    	String p1 = Game.player_1(id);
    	String p2 = Game.player_2(id);
    	
    	if(p1.equals(currentUser)){
    		if(Game.actualMoves(id)%2 != 0){
    			buildSiteForPlay(user,id);
    		}else{
    			buildSiteForWait(user,p2,id);
    		}
    	}else{
    		if(Game.actualMoves(id)%2 != 0){
	    		buildSiteForWait(user,p1,id);
    		}else{
	    		buildSiteForPlay(user,id);
    		}	
    	}
	}

	public static String winner(int id){
		Game game = Game.findFirst("id = ?",id);
		int score1 =(int)game.get("scorePlayer1");
		int score2 =(int)game.get("scorePlayer2");
		totalScoreForPlayer(id);
		String win = ""; 
		if(score1>score2){
			win = game.getString("player1");
		}else if(score2>score1){
				win = game.getString("player2");
			}else{
				win = "empate";
			}
		return win;
	}

	public static void answer(Session user,int id,String message,String currentUser){
		String msg = "";
	    if(Game.answer(id,message)){
	    	Game.currentScore(id);
	    	msg = "Respuesta Correcta";
	    }else{
	    	msg = "Respuesta Incorrecta";
	    	Game.updateMoves(id);
	   	} 

	   	String p1 = Game.player_1(id);
    	String p2 = Game.player_2(id);
    	String aux = "";

	   	if(p1.equals(currentUser)){
    		aux = EchoWebSocket.biMapUsername.inverse().get(p2);
    	}else{
    		aux = EchoWebSocket.biMapUsername.inverse().get(p1);
    	}

	   	try {
	    	user.getRemote().sendString(String.valueOf(new JSONObject()
	            .put("results",msg)
	            .put("play","yes")
	            .put("answer",Question.getAnswer(id))
		    ));
	    } catch (Exception e) {
		    e.printStackTrace();
		}
		if(aux!=null){
    		org.eclipse.jetty.websocket.api.Session user_aux = EchoWebSocket.biMapSession.get(aux);
    		App.broadcastMessage(message = "build", user_aux);
    	}
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
		String user1 = game.getString("player1");
		User p1 = User.findFirst("username = ?",user1);

		if(typeOfGame.equals("2PLAYER")){
			int scorePlayer1 = (int)game.get("scorePlayer1");
			int scorePlayer2 = (int)game.get("scorePlayer2");
			String user2 = game.getString("player2");
			User p2 = User.findFirst("username = ?",user2);
			int scoreTotalPlayer1 = (int)p1.get("score");
			int scoreTotalPlayer2 = (int)p2.get("score");

			if(scorePlayer1>scorePlayer2)
				p1.set("score",scoreTotalPlayer1+15);
			else{
				if(scorePlayer1<scorePlayer2)
					p2.set("score",scoreTotalPlayer2+15);
				else{
					p1.set("score",scoreTotalPlayer1+scorePlayer1);
					p2.set("score",scoreTotalPlayer2+scorePlayer2);
				}
				p1.saveIt();
				p2.saveIt();
			}
		}else{
			int scorePlayer1 = (int)game.get("scorePlayer1");
			int scoreTotalPlayer1 = (int)p1.get("score");
			p1.set("score",scoreTotalPlayer1+scorePlayer1);
			p1.saveIt();
		}
	}
}