package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.ArrayList;
import trivia.User;
import trivia.Question;
import trivia.Game;

public class Question extends Model{

    static{
        validatePresenceOf("description").message("Please, provide your description");
        validatePresenceOf("option1").message("Please, provide your option1");
        validatePresenceOf("option2").message("Please, provide your option2");
        validatePresenceOf("option3").message("Please, provide your option3");
        validatePresenceOf("option4").message("Please, provide your option4");
        validatePresenceOf("category").message("Please, provide your category");
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
     *  op4 != null.
     * @post. true is returned if the question was created correctly otherwise
     * false.
     */
    public static Boolean createQuestion(String cat,String desc,String op1,
                                         String op2,String op3,String op4){
        Question q = new Question();
        q.set("category",cat);
        System.out.println(cat);
        q.set("description",desc);
        q.set("option1",op1);
        q.set("option2",op2);
        q.set("option3",op3);
        q.set("option4",op4);
        Boolean res = q.save();
        return res;
    }

    /** 
     * function that returns a List of the options associated with a question in a GameHandling.random order.
     * @param description is the description of an existing question in db.
     * @return a List of the options associated with a question in a GameHandling.random order.
     * @pre. the description is associated with a question in db.
     * @post. returns a List of the options associated with a question in a GameHandling.random order.
     */
    public static List<String> mergeOptions(String description){
	    List<String> options = new ArrayList<String>();
	    Question q = Question.getQuestionByDesc(description);
	    options.add(q.getString("option1"));
	    options.add(q.getString("option2"));
	    options.add(q.getString("option3"));
	    options.add(q.getString("option4"));
	    int n = -1;
	    String auxOp = "";
	    for (int i=0;i<4;i++){
	      	n = GameHandling.random(0,3);
	      	auxOp = options.remove(n);
	      	options.add(auxOp);
	    }
	    return options;
    }

     /** 
     * function that returns a list with all the questions belonging to a given category.
     * @param nCat is a number corresponding to a category.
     * @return a list with all the questions belonging to a given category.
     * @pre. 1<= nCat <= 5 
     * @post. returns a list with all the questions belonging to a given category.
     */
    private static List<Question> getQuestionsByCategory(String nCat){
        List<Question> questions = where("category = '"+nCat+"'");
        return questions;
    }

    /** 
     * function that returns a model associated with a question in the db.
     * @return a model associated with a question in db.
     * @pre. there should be at least one question from each categoty in the db.
     * @post. create a returns a model associated with a question in the db.
     */
    public static String getQuestion(){
        int n = GameHandling.random(1,5);
        List<Question> questions = getQuestionsByCategory(getCat(n));
        n = questions.size();
        Question q = questions.get(GameHandling.random (0,n));
        String res = q.getString("description");
        return res;
    }

    /** 
     * function that returns a model associated with a question in the db.
     * @param description is the description of an existing question in db.
     * @return a model associated with a question in the db.
     * @pre. the description is associated with a question in db.
     * @post. create a returns a model associated with a question in the db.
     */
    public static Question getQuestionByDesc(String description){
        List<Question> questions = where("description = '"+description+"'");
        return questions.get(0);
    }

    /** 
     * function that returns the name of one category associated with the given parameter.
     * @param n is a number corresponding to a category.
     * @return the name of one category associated with the given parameter.
     * @pre. 1<= n <= 5.
     * @post. returns the name of one category associated with the given parameter.
     */
    private static String getCat(int n){
        String res = "";
        switch (n) {
        case 1:
            res = "CIENCIAS";
        break;

        case 2:
            res = "DEPORTES" ;
        break;

        case 3:
            res = "ENTRETENIMIENTO";
        break;

        case 4:
            res = "GEOGRAFIA";
        break;

        case 5:
            res = "HISTORIA";
        break;
        }
        return res;
    }

    public static String getAnswer(int idGame){
        Game game = Game.findFirst("id = ?", idGame);
        String desc = game.getString("question");
        Question q = getQuestionByDesc(desc);
        return q.getString("option1");
    }
}
