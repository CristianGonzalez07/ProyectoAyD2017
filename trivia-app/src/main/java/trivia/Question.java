package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.Random;

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
     *  op4 != null.
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

    private static List<Question> getQuestionsByCategory(String nCat){
        List<Question> questions = where("category = '"+nCat+"'");
        return questions;
    }//End getQuestionByCategory

    //obtiene una pregunta aleatoria
    public static Question getQuestion(){
        int n = random(1,5);
        List<Question> questions = getQuestionsByCategory(getCat(n));
        n = questions.size();
        Question q = questions.get(random (0,n));
        return q;
    }
    //obtiene una pregunta especifica
    public static Question getQuestionByDesc(String description){
        List<Question> questions = where("description = '"+description+"'");
        return questions.get(0);
    }

    public static String getAnswer(Question q){
        return q.getString("option1");
    }

    //retorna el string correspondiente al nro de cat tomado como parametroX!
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
} //End class Question
