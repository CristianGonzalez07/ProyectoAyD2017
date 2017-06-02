package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;
import trivia.Question;
import trivia.Game;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
      Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

    	Map map = new HashMap();

      get("/", (req, res) -> {
        return new ModelAndView(map, "./views/mainpage.mustache");
      }, new MustacheTemplateEngine()
      );

      get("/login", (req, res) -> {
        return new ModelAndView(map, "./views/login.mustache");
      }, new MustacheTemplateEngine()
      );

      post("/login", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        //User usuario1 = User.createIt("username",request.queryParams("txt_username"),"password",request.queryParams("txt_password"));
        User usuario1 = User.create("username",request.queryParams("txt_username"),"password",request.queryParams("txt_password"));
        if(!usuario1.save()){
        	map.put("message","error");
        	response.redirect("/login");
        }else{
        	response.redirect("/");
        }
        Base.close();
        return null;   
    });
  }       
}
