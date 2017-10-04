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
	private static final String SESSION_NAME = "username";
	private static Boolean conect = false;

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

	
    public static void main( String[] args )
    {	
    	staticFileLocation("/views");
    	Map map = new HashMap();


    	before((req, res)->{
    		conect = !(conect);
        	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
   		});

    	after((req, res) -> {
      		Base.close();
      		conect = !(conect);
    	});

	    get("/", (request, response) -> {
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
	        return new ModelAndView(map, "./views/gameMenu.mustache");
	    },   new MustacheTemplateEngine()
	    ); 

 	    get("/play", (request, response) -> {
            String username = (String)request.session().attribute(SESSION_NAME);
	      	String description = Game.newQuestion(username);
	      	List<String> options = Question.mergeOptions(description);
	      	map.put("question",description);
	      	map.put("option1",options.get(0));
	      	map.put("option2",options.get(1));
	      	map.put("option3",options.get(2));
	      	map.put("option4",options.get(3));
	        return new ModelAndView(map, "./views/play.mustache");
        },  new MustacheTemplateEngine()
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

		post("/play", (request,response) -> {
			String username = (String)request.session().attribute(SESSION_NAME);
			String currentAnswer = request.queryParams("btn_option");
				      	
			if(Game.answer(username).equals(currentAnswer)){
			   	map.put("msgResult1","Respuesta Correcta");
			   	map.put("msgResult2","");
			   	int score = Game.currentScore(username);
			   	map.put("score",score);
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
