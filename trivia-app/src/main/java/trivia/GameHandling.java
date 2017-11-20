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
					user.getRemote().sendString(String.valueOf(new JSONObject()
						.put("endGame","true")
						.put("winner",win)
						.put("typeOfGame","1Player")
						.put("scorep1",Game.getScoreP1(id))
						.put("scorep2",Game.getScoreP2(id))
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
					user.getRemote().sendString(String.valueOf(new JSONObject()
						.put("endGame","true")
						.put("winner",win)
						.put("typeOfGame","1Player")
						.put("scorep1",Game.getScoreP1(id))
						.put("scorep2",Game.getScoreP2(id))
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
}