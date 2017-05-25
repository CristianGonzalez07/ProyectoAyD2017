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

    //retorna un nro aleatorio en el intervalo tomado como parametro(considerando extremos)
    private static int random(int init,int end) {
        Random  rnd = new Random();
        return (int)(rnd.nextDouble() * end + init);
    }

    private static List<Question> getQuestionsByCategory(String nCat){
        List<Question> questions = where("category = '"+nCat+"'");
        return questions;
    }//End getQuestionByCategory

    public static void loadQuestion(String category,String description,String op1,String op2,String op3,String op4){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Question q = Question.createIt("category",category,"description",description,"option1",op1,"option2",op2,"option3",op3,"option4",op4);
        Base.close();
    } //End loadQuestion

    //permite al usuario respoder una pregunta y retorna si la rta es correcta o no
    public static Boolean responseQuestion(String category){
        List<Question> questions = getQuestionsByCategory(category);
        int n = questions.size();
        Question q = questions.get(random (0,n));
        //la opcion 1 es siempre la correcta en la base de datos
        String correct = q.getString("option1");
        if(correct.equals(getAnswer(q))){
            return true;
        }else{
            return false;
        }
    }//end responseQuestion

    private static String getAnswer(Question q){
        return q.getString("option1");
    }

} //End class Question
