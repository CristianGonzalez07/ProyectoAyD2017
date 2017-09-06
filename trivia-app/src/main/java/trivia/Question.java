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

    private static List<Question> getQuestionsByCategory(String nCat){
        List<Question> questions = where("category = '"+nCat+"'");
        return questions;
    }//End getQuestionByCategory

    //obtiene una pregunta aleatoria
    public static Question getQuestion(){
        int n = Lib.random(1,5);
        List<Question> questions = getQuestionsByCategory(getCat(n));
        n = questions.size();
        Question q = questions.get(Lib.random (0,n));
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
