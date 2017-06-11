package trivia;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import trivia.User;
import trivia.Question;
import trivia.Game;

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

	//retorna un nro aleatorio en el intervalo tomado como parametro(considerando extremos)
	private static int random(int init,int end) {
		Random  rnd = new Random();
		return (int)(rnd.nextDouble() * end + init);
	}

	private static final String SESSION_NAME = "username";

    public static void main( String[] args )
    {	
    	Map map = new HashMap();

    	before((request,response) ->{
    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    	}
    	);

    	after((request,response) ->{
    		Base.close();
    	}
    	);

	      //pagina de Inicio
	      get("/", (request, response) -> {
	      	request.session().removeAttribute(SESSION_NAME);
	      	map.clear();
	        return new ModelAndView(map, "./views/mainpage.mustache");
	      }, new MustacheTemplateEngine()
	      );

	      //pagina de inicio Sesion
	      get("/login", (reqquest, response) -> {
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
	        return new ModelAndView(map, "./views/gameMenu.mustache");
	      }, new MustacheTemplateEngine()
	      );

	       //pagina de juego
	      get("/play", (request, response) -> {
	      	Question q = Question.getQuestion();
	      	map.put("question",q.get("description"));
	      	List<String> options = new ArrayList<String>();
	      	options.add(q.getString("option1"));
	      	options.add(q.getString("option2"));
	      	options.add(q.getString("option3"));
	      	options.add(q.getString("option4"));
	      	int n = -1;
	      	String auxOp = "";
	      	for (int i=0;i<4;i++){
	      		n = random(0,3);
	      		auxOp = options.remove(n);
	      		options.add(auxOp);
	      	}
	      	map.put("option1",options.get(0));
	      	map.put("option2",options.get(1));
	      	map.put("option3",options.get(2));
	      	map.put("option4",options.get(3));
	        return new ModelAndView(map, "./views/play.mustache");
	      }, new MustacheTemplateEngine()
	      );

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
	      post("/register", (request, response) -> {
	      	User user = User.create("username",request.queryParams("txt_username"),"password",request.queryParams("txt_password"));
	      	Boolean res = user.save(); 
	        if(!res)
	        {
	        	map.put("msgFailRegis","Usuario no valido o en uso/contraseña no valida");
	        	map.put("msgSucessRegis","");
	        	response.redirect("/register");	
	        }else{
	      		map.put("msgFailRegis","");
	        	map.put("msgSucessRegis","Usuario Registrado Exitosamente");
	        	response.redirect("/register");
	        }
	        return null;
	      });
	      
	      //Iniciar sesion
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
		    	map.put("currentUser",username);
                request.session().attribute(SESSION_NAME, username);
			    if(permissions != null){
			    	response.redirect("/admin");
			    }else{
			    	response.redirect("/gameMenu");
			   	}
	        }
	        return null;      
	      });

	      //Crear Pregunta
	      post("/createQuestion", (request, response) -> {
	      	map.clear();
        	Question q = new Question();
        	q.set("category",request.queryParams("Category"));
        	System.out.println(request.queryParams("Category"));
        	q.set("description",request.queryParams("Description"));
        	q.set("option1",request.queryParams("txt_op1"));
        	q.set("option2",request.queryParams("txt_op2"));
        	q.set("option3",request.queryParams("txt_op3"));
        	q.set("option4",request.queryParams("txt_op4"));
        	Boolean res = q.save();
        	System.out.println(res);
	      	if(!res){
	      		map.put("msgFailCreateQuestion","alguno de los datos ingresados no es valido.Cargue de nuevo la pregunta");
	      		response.redirect("/createQuestion");
	      	}else{
	      		map.put("msgSucessCreateQuestion","pregunta cargada con exito");
	      		response.redirect("/createQuestion");
	      	}
	        return null;
	      });

	      //Jugar
	      post("/play", (request,response) -> {
	      	System.out.println((String)request.session().attribute(SESSION_NAME));
	      	String description =(String)map.get("question");
	      	Question q = Question.getQuestionByDesc(description);	
	      	String currentAnswer = request.queryParams("btn_option");
	      	if(Question.getAnswer(q).equals(currentAnswer)){
	      		map.put("msgResult1","Respuesta Correcta");
	      		map.put("msgResult2","");
	      		System.out.println((String)request.session().attribute(SESSION_NAME));
	      		User.calcularPuntaje(request.session().attribute(SESSION_NAME));
	      		response.redirect("/results");
	      	}else{
	      		map.put("msgResult2","Respuesta Incorrecta");
	      		map.put("msgResult1","");
	      		response.redirect("/results");
	      	}
	      	return null;
	      });
  	}       
}
