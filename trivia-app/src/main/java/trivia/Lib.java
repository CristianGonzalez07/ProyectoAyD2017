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

/**
 *	This Class this class implements methods related to the management of
 *  database models
 *	@author Bea,David.
 *	@author Gonzalez,Cristian.
 *	@author Saez,Sebastian.
 *	@version 0.1, 09/09/2017
 */

public class Lib{

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

	/** 
     * function that return true if the question was created correctly
     * otherwise false.
     * @param cat is the category corresponding to the question to be 
     * created.
     * @param desc is the description corresponding to the question to be 
     * created.
     * @param op1 is the first corresponding to the question to be created.
     * @param op2 is the second corresponding to the question to be created.
     * @param op3 is the third corresponding to the question to be created.
     * @param op4 is the fourth corresponding to the question to be created.
     * @return true if the question was created correctly otherwise false
     * @pre. cat != null. desc != null. op1 != null. op2 != null. 0p3 != null.
     *	op4 != null.
     * @post. true is returned if the question was created correctly otherwise
     * false.
     */
	public static Boolean createQuestion(String cat,String desc,String op1,
										 String op2,String op3,String op4){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia",
				 "root", "root");
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