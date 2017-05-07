package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;

/**
 * Hello Pregunta2
 *
 */
public class App
{
    public static void main( String[] args )
    {
     
     Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
      User u = new User();
      Base.close();
    }
}