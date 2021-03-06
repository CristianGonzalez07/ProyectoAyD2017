package trivia;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import trivia.EchoWebSocket;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static spark.Spark.*;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App
{
	private static final String SESSION_NAME = "username";

	static int nextUserNumber = 1;

//Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String message,Session user) {
    	if(!Base.hasConnection()) {
    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    	}

    	String aux = EchoWebSocket.biMapSession.inverse().get(user);
    	String currentUser = EchoWebSocket.biMapUsername.get(aux);
    	System.out.println("===??????????"+currentUser);
    	int id = (int)User.getCurrentGameId(currentUser);
    	String typeOfGame = Game.getCurrentGameType(id);

    	if(message.equals("build")){
    		if(typeOfGame.equals("1PLAYER")){
    			GameHandling.buildSiteForPlay(user,id);
    		}
    		else if(typeOfGame.equals("2PLAYER")){
    			GameHandling.buildSiteFor2Players(user,id,currentUser);
    		}

    	}else{ //el msj es una respuesta a una pregunta
    		GameHandling.answer(user,id,message,currentUser);
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
	    				if(!Base.hasConnection()) {
    						Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    					}
	        			Game.checkWaitTime();
	        			Game.checkProgresGame();
	        			if(Base.hasConnection()){
    						Base.close();
    					}
	     			}
				});

		timer.start();

		

		 get("/error", (request, response) -> {
	       return new ModelAndView(map, "./views/error.html");
	    }, new MustacheTemplateEngine()
	    );

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
	    	if(request.session().attribute(SESSION_NAME)!=null){
	    		map.clear();
	    		List<String> ranking = User.ranking();
	    		for(int i=0;i<5;i++){
		        		map.put("posicion"+(i+1),ranking.get(i));
		        	}

	    		String username = (String)request.session().attribute(SESSION_NAME);
	            map.put("currentUser",username);
	            map.put("score",User.getScore(username));
		        if(Invitation.newInvitations(username)){
		        	map.put("textoInvitaciones","Los siguientes usuarios te han invitado a una partida.Para aceptar o rechazar has click sobre el nombre del usuario");
		        	List<String> invitations = Invitation.getInvitations(username);
		        	for(int i=0;i<3 && i<invitations.size();i++){
		        		map.put("Invitacion"+(i+1),invitations.get(i));
		        	}
		        }
	    	}else{
	    		response.redirect("error");
	    	}

	         return new ModelAndView(map, "./views/gameMenu.mustache");
	    },   new MustacheTemplateEngine()
	    );



        get("/createQuestion", (request, response) -> {
        	if(request.session().attribute(SESSION_NAME)==null){
        		response.redirect("/error");
        	}
	        return new ModelAndView(map, "./views/createQuestion.mustache");
        },  new MustacheTemplateEngine()
        );

        get("/admin", (request, response) -> {
        	if(request.session().attribute(SESSION_NAME)==null){
        		response.redirect("/error");
        	}
	        return new ModelAndView(map, "./views/admin.mustache");
        },  new MustacheTemplateEngine()
        );

        get("/continue", (request, response) -> {
        	if(request.session().attribute(SESSION_NAME)!=null){
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
        	}else{
        		response.redirect("/error");
        	}
	        return new ModelAndView(map, "./views/continue.mustache");
        },  new MustacheTemplateEngine()
        );

				get("/pairingForSearch", (request, response) -> {
					if(request.session().attribute(SESSION_NAME)==null){
        		response.redirect("/error");
        	}
	        return new ModelAndView(map, "./views/pairingForSearch.mustache");
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
	    	String randomPairing = request.queryParams("randomPairing");
	    	String searchPairing = request.queryParams("SearchPairing");
	      	String logOut = request.queryParams("Logout");
	      	String game = request.queryParams("game");
	      	String invitation = request.queryParams("invitacion");
	      	String username = (String)request.session().attribute(SESSION_NAME);

	      	if((typeOfGame != null) || (randomPairing != null) || (searchPairing != null)){
	      		if(Game.limitGames(username)){
	      			if("1 Jugador".equals(typeOfGame)){
		      			Game.createGame1Player(username);
		      			EchoWebSocket.biMapUsername.put("user"+nextUserNumber,username);
		      			response.redirect("/");
			      	}else if(randomPairing != null){
			      		String player2 = User.randomMatch(username);
			      		Invitation.createInvitation(username,player2);
			      		Game.createGame2Player(username,player2);
			      		map.put("msgSucess","Esperando confirmacion de partida.Para saber si la partida fue aceptada ingrese en reanudar Partida");
			      		response.redirect("/gameMenu");
			      	}else if(searchPairing != null){
						map.clear();
			      	}
	      		}else{
	      			map.put("msgError","Usted no puede iniciar mas partidas.primero debe finalizar las partidas ya iniciadas");
	      			response.redirect("/gameMenu");
	      		}
	      	}else if(invitation != null){
		      		int id = Game.findIdGame(invitation,username);
		      		Game.startGame2Player(id);
		      		User.setCurrentGame(username,id);
		      		Invitation.deleteInvitation(invitation,username);
		      		EchoWebSocket.biMapUsername.put("user"+nextUserNumber,username);
		      		response.redirect("/");
	      		}else if(logOut!=null){
	      				response.redirect("/mainpage");
	      		}else{
	      			map.clear();
	      		}
	      	return null;
	    });

			post("/pairingForSearch", (request, response) -> {
				String username = (String)request.session().attribute(SESSION_NAME);
				String usernameInvitation = request.queryParams("txt_username");
				if(User.userExists(username,usernameInvitation)){
					map.put("msgSucessInvitation","Solicitud enviada correctamente. Esperando ser aceptada");
			        Game.createGame2Player(username,usernameInvitation);
					response.redirect("/pairingForSearch");
				}else{
					map.put("msgFailInvitation","El usuario que solicitó no se encuentra registrado o ya posee una invitacion pendiente con usted.");
					response.redirect("/pairingForSearch");
				}
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
