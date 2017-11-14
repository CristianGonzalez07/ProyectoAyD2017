package trivia;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import trivia.EchoWebSocket;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import static j2html.TagCreator.*;

import static spark.Spark.*;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App
{
	private static final String SESSION_NAME = "username";
	
	static int nextUserNumber = 1;
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



  	//Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message,Session user) {
    	if(!Base.hasConnection()) {
    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    	}

    	String aux = EchoWebSocket.biMapSession.inverse().get(user);
    	String currentUser = EchoWebSocket.biMapUsername.get(aux);

    	int id = (int)User.getCurrentGameId(currentUser);
    	String typeOfGame = User.getCurrentGameType(currentUser);
    	
    	if(message.equals("build")){
    		String question = Question.getQuestion();
    		Game.setQuestion(question,id);
    		List<String> options = Question.mergeOptions(question);	
    		
    		if(typeOfGame.equals("1PLAYER")){
    			try {
                user.getRemote().sendString(String.valueOf(new JSONObject()
                	.put("play","yes")
                    .put("question",question)
                    .put("option1",options.get(0))
                    .put("option2",options.get(1))
                    .put("option3",options.get(2))
                    .put("option4",options.get(3))
                    .put("results","")
                ));
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
    		}
    		else if(typeOfGame.equals("2PLAYER")){
    			int numberOfPlayer = Game.numberOfPlayer(id,currentUser);
    			String p1 = Game.player_1(id);
    			String p2 = Game.player_2(id);
    			org.eclipse.jetty.websocket.api.Session user1 = null;
    			org.eclipse.jetty.websocket.api.Session user2 = null;


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

    				try {
    					if(user1 != null){
		                	user1.getRemote().sendString(String.valueOf(new JSONObject()
		                		.put("play","yes")
			                    .put("question",question)
			                    .put("option1",options.get(0))
			                    .put("option2",options.get(1))
			                    .put("option3",options.get(2))
			                    .put("option4",options.get(3))
			                    .put("results","")
			                ));
		                }
		                if(user2 != null){
				    		user2.getRemote().sendString(String.valueOf(new JSONObject()
				    			.put("play","no")
				    			.put("msgEspera","Espere...El turno de responder es de: "+p1)
				    		));	
		           		}
		            } catch (Exception e) {
		                e.printStackTrace();
		            }

    			}else{
    				try {
    					if(user1 != null){
		                	user1.getRemote().sendString(String.valueOf(new JSONObject()
			                    .put("play","no")
				    			.put("msgEspera","Espere...El turno de responder es de: "+p2)
			                ));
	                	}
	                	if(user2 != null){
			    			user2.getRemote().sendString(String.valueOf(new JSONObject()
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
    		}

    	}else{ //el msj es una respuesta a una pregunta
	    	String msg = "";
	    	if(Game.answer(id,message)){
	    		Game.currentScore(id);
	    		msg = "Respuesta Correcta";
	    	}else{
	    		msg = "Respuesta Incorrecta";
	    		Game.updateMoves(id);
	   		}
	   		try {
	    		user.getRemote().sendString(String.valueOf(new JSONObject()
	                    .put("results",msg)
	                    .put("play","yes")
		        ));
	        } catch (Exception e) {
		        e.printStackTrace();
		    }
	   	}
	
        if(Base.hasConnection()){
    		Base.close();
    	}
    }


    public static void main( String[] args )
    {	
    	staticFileLocation("/views");
    	Map map = new HashMap();
    	staticFiles.expireTime(6000);
        webSocket("/chat", EchoWebSocket.class);
        init();

    	before((request, response)->{
    		if(!Base.hasConnection()) {
    			Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    		}
        });

    	after((request, response) -> {
    		if(Base.hasConnection()) {
    			Base.close();
    		}
    	});

    	Timer timer = new Timer (960000, new ActionListener () 
				{ 
	    			public void actionPerformed(ActionEvent e) 
	    			{ 
	        			Game.checkWaitTime();
	        			Game.checkProgresGame();
	     			} 
				}); 

		timer.start();

	    get("/mainpage", (request, response) -> {
	       request.session().removeAttribute(SESSION_NAME);
	       map.clear();
	       return new ModelAndView(map, "./views/mainpage.mustache");
	    }, new MustacheTemplateEngine()
	    );

	    get("/login", (request, response) -> {
	       return new ModelAndView(map, "./views/login.mustache");
	    }, new MustacheTemplateEngine()
	    );

	    get("/register", (request, response) -> {
	       return new ModelAndView(map, "./views/register.mustache");
	    }, new MustacheTemplateEngine()
	    );

	    get("/gameMenu", (request, response) -> {
            String username = (String)request.session().attribute(SESSION_NAME);
            map.put("currentUser",username);
	        if(Invitation.newInvitations(username)){
	        	map.put("textoInvitaciones","Los siguientes usuarios te han invitado a una partida.Para aceptar o rechazar has click sobre el nombre del usuario");
	        	List<String> invitations = Invitation.getInvitations(username);
	        	for(int i=0;i<3 && i<invitations.size();i++){
	        		map.put("Invitacion"+(i+1),invitations.get(i));
	        	}
	        }
	         return new ModelAndView(map, "./views/gameMenu.mustache");
	    },   new MustacheTemplateEngine()
	    ); 

        get("/createQuestion", (request, response) -> {
	        return new ModelAndView(map, "./views/createQuestion.mustache");
        },  new MustacheTemplateEngine()
        );

        get("/admin", (request, response) -> {
	        return new ModelAndView(map, "./views/admin.mustache");
        },  new MustacheTemplateEngine()
        );

        get("/results", (request, response) -> {
            return new ModelAndView(map, "./views/results.mustache");
        }, new MustacheTemplateEngine()
        );

        get("/continue", (request, response) -> {
        	String username = (String)request.session().attribute(SESSION_NAME);
	        if(User.games(username)){
	        	List<String> games = Game.games(username);
	        		map.put("textoPartidas","Las siguientes Partidas estan Activas·Has click sobre ellas para reanudar");
	        		for(int i=0;i<3 && i<games.size();i++){
	        			map.put("Partida"+(i+1),games.get(i));
	        		}
	        }else{
        		map.put("textoPartidas","Usted no posee Partidas Activas en este momento");
        	}
	        return new ModelAndView(map, "./views/continue.mustache");
        },  new MustacheTemplateEngine()
        );

	    post("/register", (request, response) -> {
	      	String username = request.queryParams("txt_username");
	      	String password = request.queryParams("txt_password");
	      	int res = User.register(username,password);

	        if(res == 1){
	        	map.put("msgFailRegis","Usuario no disponible");
		        map.put("msgSucessRegis","");
		        response.redirect("/register");	
	        }
	        if(res == 2){
		        	map.put("msgFailRegis","Usuario no valido o en uso/contraseña no valida");
		        	map.put("msgSucessRegis","");
		        	response.redirect("/register");	
		    }
		    if(res == 3){
		      		map.put("msgFailRegis","");
		        	map.put("msgSucessRegis","Usuario Registrado Exitosamente");
		        	response.redirect("/register");
	        }
	        return null;
	    });

	    post("/login", (request, response) -> {
	      	String username = request.queryParams("txt_username");
	      	String password = request.queryParams("txt_password");
	      	String permissions = request.queryParams("permissions");
	      	Boolean res = User.validateLogin(username,password,permissions);
	      	
		    if(!res){
		        map.put("msgLogin","Usuario no valido /contraseña no valida");
		        response.redirect("/login");	
		    }else{	
		    	User user = User.findFirst("username = ?",username);
		    	map.put("score",user.get("score"));
		    	request.session(true);
		    	request.session().attribute(SESSION_NAME,username);
			    if(permissions != null){
			    	response.redirect("/admin");
			    }else{
			    	response.redirect("/gameMenu");
			   	}
	        }
	        return null;      
	      });

	    post("/createQuestion", (request, response) -> {
	      	map.clear();

            String cat = request.queryParams("Category");
	      	String desc = request.queryParams("Description");
	      	String op1 = request.queryParams("txt_op1");
	      	String op2 = request.queryParams("txt_op2");
	      	String op3 = request.queryParams("txt_op3");
	      	String op4 = request.queryParams("txt_op4");

	      	Boolean res = Question.createQuestion(cat,desc,op1,op2,op3,op4);
	      	
	      	if(!res){
	      		map.put("msgFailCreateQuestion","alguno de los datos ingresados no es valido.Cargue de nuevo la pregunta");
	      		response.redirect("/createQuestion");
	      	}else{
	      		map.put("msgSucessCreateQuestion","pregunta cargada con exito");
	      		response.redirect("/createQuestion");
	      	}
	        return null;
	      });

	    post("/gameMenu", (request, response)->{
	    	String typeOfGame = request.queryParams("typeOfGame");
	      	String logOut = request.queryParams("Logout");
	      	String game = request.queryParams("game");
	      	String invitation = request.queryParams("invitacion");
	      	String username = (String)request.session().attribute(SESSION_NAME);
	      	
	      	if(typeOfGame != null){
	      		if(Game.limitGames(username)){
	      			if(typeOfGame.equals("1 Jugador")){
		      			Game.createGame1Player(username);
		      			EchoWebSocket.biMapUsername.put("user"+nextUserNumber,username);
		      			response.redirect("/");
			      	}else if(typeOfGame.equals("2 Jugadores")){
			      		String player2 = User.randomMatch(username);
			      		Invitation.createInvitation(username,player2);
			      		Game.createGame2Player(username,player2);
			      		response.redirect("/gameMenu");
			      	}
	      		}else{
	      			map.put("msgError","Usted no puede iniciar mas partidas.primero debe finalizar las partidas ya iniciadas");
	      			response.redirect("/gameMenu");	
	      		} 		
	      	}

	      	if(invitation != null){
	      		int id = Game.findIdGame(invitation,username);
	      		Game.startGame2Player(id);
	      		User.setCurrentGame(username,id);
	      		Invitation.deleteInvitation(invitation,username);
	      		EchoWebSocket.biMapUsername.put("user"+nextUserNumber,username);
	      		response.redirect("/");
	      	}

	      	if(logOut!=null){
	      		response.redirect("/mainpage");
	      	}
	      	map.clear();
	      	return null;
	    });
  	
	   post("/continue", (request, response)->{
		   	String username = (String)request.session().attribute(SESSION_NAME);
		   	String game = request.queryParams("Partida");
		   	if(game != null){
		   		int id = Integer.parseInt(game);
	      		User.setCurrentGame(username,id);
	      		EchoWebSocket.biMapUsername.put("user"+nextUserNumber,username);
	      		response.redirect("/");
	      	}
  			return null;
  	   });   
  	}   
}
