package trivia;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import trivia.User;
import trivia.Question;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static spark.Spark.*;

public class Lib{

	public static int random(int init,int end) {
		Random  rnd = new Random();
		return (int)(rnd.nextDouble() * end + init);
	}

	public static Boolean createQuestion(String cat,String desc,String op1,String op2,String op3,String op4){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        	Question q = new Question();
        	q.set("category",cat );
        	System.out.println(cat);
        	q.set("description",desc);
        	q.set("option1",op1);
        	q.set("option2",op2);
        	q.set("option3",op3);
        	q.set("option4",op4);
        	Boolean res = q.save();
        Base.close();
        	return res;

	}

}