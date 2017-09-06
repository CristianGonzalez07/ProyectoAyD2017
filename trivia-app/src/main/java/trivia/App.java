package trivia;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import trivia.User;
import trivia.Question;
import trivia.Lib;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static spark.Spark.*;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App
{
	private static final String SESSION_NAME = "username";

    public static void main( String[] args )
    {	
    	staticFileLocation("/views");
    	Map map = new HashMap();

	      //pagina de Inicio
	      get("/", (request, response) -> {
	      	request.session().removeAttribute(SESSION_NAME);
	      	map.clear();
	        return new ModelAndView(map, "./views/mainpage.mustache");
	      }, new MustacheTemplateEngine()
	      );

	      //pagina de inicio Sesion
	      get("/login", (request, response) -> {
	        return new ModelAndView(map, "./views/login.mustache");
	      }, new MustacheTemplateEngine()
	      );

	      //pagina de crear cuenta
	      get("/register", (request, response) -> {
	        return new ModelAndView(map, "./views/register.mustache");
	      }, new MustacheTemplateEngine()
	      );

	       //pagina de Usuario 
	      get("/gameMenu", (request, response) -> {
			String username = (String)request.session().attribute(SESSION_NAME);
	      	map.put("currentUser",username);
	        return new ModelAndView(map, "./views/gameMenu.mustache");
	      }, new MustacheTemplateEngine()
	      );

	       //pagina de juego
 //-------------------MODULARIZAR---------------------------------------------
	      get("/play", (request, response) -> {
	      	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
	      	Question q = Question.getQuestion();
	      	String username = (String)request.session().attribute(SESSION_NAME);
	      	String description = q.getString("description"); 
	      	Game game = new Game();
	      	List<Game> games  = Game.where("user ='"+username+"'");
	      	if(games.size()!=0){
	      		game = Game.findFirst("user = ?",username);
	      	}else{
	      		game.set("user",username);
	      	}
	      	game.set("description",description);
	      	game.saveIt();	
	      	List<String> options = new ArrayList<String>();
	      	options.add(q.getString("option1"));
	      	options.add(q.getString("option2"));
	      	options.add(q.getString("option3"));
	      	options.add(q.getString("option4"));
	      	int n = -1;
	      	String auxOp = "";
	      	for (int i=0;i<4;i++){
	      		n = Lib.random(0,3);
	      		auxOp = options.remove(n);
	      		options.add(auxOp);
	      	}
	      	map.put("question",description);
	      	map.put("option1",options.get(0));
	      	map.put("option2",options.get(1));
	      	map.put("option3",options.get(2));
	      	map.put("option4",options.get(3));
	      	Base.close();
	        return new ModelAndView(map, "./views/play.mustache");
	      }, new MustacheTemplateEngine()
	      );
//-------------------MODULARIZAR---------------------------------------------


	      //pagina de creacion de pregunta
	      get("/createQuestion", (request, response) -> {
	        return new ModelAndView(map, "./views/createQuestion.mustache");
	      }, new MustacheTemplateEngine()
	      );

	      //pagina de administrador
	      get("/admin", (request, response) -> {
	        return new ModelAndView(map, "./views/admin.mustache");
	      }, new MustacheTemplateEngine()
	      );

	      //pagina de Resultados 
	      get("/results", (request, response) -> {
	        return new ModelAndView(map, "./views/results.mustache");
	      }, new MustacheTemplateEngine()
	      );

	      //crear cuenta
	//-------------------MODULARIZAR---------------------------------------------
	      post("/register", (request, response) -> {
	      	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
	      	String username = request.queryParams("txt_username");
	        List<User> users  = User.where("username ='"+username+"'");
	        if(users.size()!=0){
	        	map.put("msgFailRegis","Usuario no disponible");
		        map.put("msgSucessRegis","");
		        response.redirect("/register");	
	        }else{
	        	String password = request.queryParams("txt_password");
	        	User user = User.create("username",request.queryParams("txt_username"),"password",request.queryParams("txt_password"));
	      		Boolean res = user.save();
	        	if(!res){
		        	map.put("msgFailRegis","Usuario no valido o en uso/contraseña no valida");
		        	map.put("msgSucessRegis","");
		        	response.redirect("/register");	
		        }else{
		      		map.put("msgFailRegis","");
		        	map.put("msgSucessRegis","Usuario Registrado Exitosamente");
		        	response.redirect("/register");
		        }
	        }
	        Base.close();
	        return null;
	      });
	//-------------------MODULARIZAR---------------------------------------------


	      //Iniciar sesion
	//-------------------MODULARIZAR---------------------------------------------
	      post("/login", (request, response) -> {
	      	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
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
	        Base.close();
	        return null;      
	      });
	//-------------------MODULARIZAR---------------------------------------------


	      //Crear Pregunta
	//-------------------MODULARIZAR---------------------------------------------
	      post("/createQuestion", (request, response) -> {
	      	map.clear();

	      	String cat = request.queryParams("Category");
	      	String desc = request.queryParams("Description");
	      	String op1 = request.queryParams("txt_op1");
	      	String op2 = request.queryParams("txt_op2");
	      	String op3 = request.queryParams("txt_op3");
	      	String op4 = request.queryParams("txt_op4");

	      	Boolean res = Lib.createQuestion(cat,desc,op1,op2,op3,op4);
	      	
	      	if(!res){
	      		map.put("msgFailCreateQuestion","alguno de los datos ingresados no es valido.Cargue de nuevo la pregunta");
	      		response.redirect("/createQuestion");
	      	}else{
	      		map.put("msgSucessCreateQuestion","pregunta cargada con exito");
	      		response.redirect("/createQuestion");
	      	}
	        return null;
	      });
	//-------------------MODULARIZAR---------------------------------------------

	      //Jugar
	//-------------------MODULARIZAR---------------------------------------------
	      post("/play", (request,response) -> {
	      	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
	      	String username = (String)request.session().attribute(SESSION_NAME);
	      	Game game = Game.findFirst("user = ?",username);
	      	User user = User.findFirst("username = ?",username);
	      	String description =game.getString("description");
	      	Question q = Question.getQuestionByDesc(description);	
	      	String currentAnswer = request.queryParams("btn_option");
	      	if(Question.getAnswer(q).equals(currentAnswer)){
	      		map.put("msgResult1","Respuesta Correcta");
	      		map.put("msgResult2","");
	      		int score = (int)user.get("score");
  				user.set("score",(score+1));
  				user.saveIt();
	      		map.put("score",user.get("score"));
	      		response.redirect("/results");
	      	}else{
	      		map.put("msgResult2","Respuesta Incorrecta");
	      		map.put("msgResult1","");
	      		response.redirect("/results");
	      	}
	      	Base.close();
	      	return null;
	      });
  	//-------------------MODULARIZAR--------------------------------------------- 
  	}     
}
