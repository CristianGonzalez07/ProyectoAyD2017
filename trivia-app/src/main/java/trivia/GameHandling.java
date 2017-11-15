package trivia;
import trivia.EchoWebSocket;
import trivia.Game;
import trivia.Question;
import java.util.List;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;

public class GameHandling{

	public static void buildSiteForPlay(Session user,int id){
		String question = Question.getQuestion();
    	Game.setQuestion(question,id);
    	List<String> options = Question.mergeOptions(question);	
		try {
			if(user != null){
	            user.getRemote().sendString(String.valueOf(new JSONObject()
	                .put("play","yes")
	                .put("question",question)
	                .put("option1",options.get(0))
	                .put("option2",options.get(1))
	                .put("option3",options.get(2))
	                .put("option4",options.get(3))
	                .put("results","")
	            ));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void buildSiteForWait(Session user,String player){
		if(user != null){
			try {
				user.getRemote().sendString(String.valueOf(new JSONObject()
					.put("play","no")
					.put("msgEspera","Espere...El turno de responder es de: "+player)
				));	
			} catch (Exception e) {
	        	e.printStackTrace();
	    	}
		}
	}

	public static void buildSiteFor2Players(Session user,int id,String currentUser){
		int numberOfPlayer = Game.numberOfPlayer(id,currentUser);
    	String p1 = Game.player_1(id);
    	String p2 = Game.player_2(id);
    	org.eclipse.jetty.websocket.api.Session user1 = null;
    	org.eclipse.jetty.websocket.api.Session user2 = null;
    	String aux = ""; 
    	if(p1.equals(currentUser)){
    		user1 = user;
    		aux = EchoWebSocket.biMapUsername.inverse().get(p2);
		    if(aux != null){
		        user2 = EchoWebSocket.biMapSession.get(aux);
		    }
    	}else{
    		user2 = user;
    		aux = EchoWebSocket.biMapUsername.inverse().get(p1);
		    if(aux != null){
		        user1 = EchoWebSocket.biMapSession.get(aux);
		    }
    	}

    	if(Game.actualMoves(id)%2 != 0){
    		buildSiteForPlay(user1,id);
    		buildSiteForWait(user2,p1);
    	}else{
    		buildSiteForPlay(user2,id);
    		buildSiteForWait(user1,p2);
    	}

	}
}