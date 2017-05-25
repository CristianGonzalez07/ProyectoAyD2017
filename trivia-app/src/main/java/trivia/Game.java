package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import trivia.User;
import trivia.Question;
import java.util.List;
import java.util.Random;

public class Game extends Model{
	static{
    	validatePresenceOf("player1_id").message("Please, provide player1");
  		validatePresenceOf("player2_id").message("Please, provide player2");
 	}

 	//retorna un nro aleatorio en el intervalo tomado como parametro(considerando extremos)
	private static int random(int init,int end) {
		Random  rnd = new Random();
		return (int)(rnd.nextDouble() * end + init);
	}

	//crea una partida entre 2 usuarios
	public static void createGame(String user1,String user2){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		Game g = new Game();
     	List<User> users = User.where("username = ?",user1);
      	User u = users.get(0);
      	g.set("player1_id", u.get("id"));
      	User u2 = User.findFirst("username = ?",user2);
      	g.set("player2_id", u2.get("id"));
      	g.set("state","IN_PROGRESS");
      	g.set("turn",'1');
      	g.set("scorep1",0);
      	g.set("scorep2",0);
      	g.saveIt();
		Base.close();
	}

/*	//retorna el string correspondiente al nro de cat tomado como parametroX!
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

	private static void showQuestion(Question q){		//dada una pregunta, la muestra en pantalla junto con sus opciones y computa la rta
		System.out.println(q.get("question"));
		System.out.println("1- "+q.get("option1"));
		System.out.println("2- "+q.get("option2"));
		System.out.println("3- "+q.get("option3"));
		System.out.println("4- "+q.get("option4"));
	}

	private static Boolean getAnswer(Question q, String r){	//dada una pregunta y una respuesta determina si la rta es correcta o no
		if(q.get("option1").equals(r)){
			return true;
		}else{
			return false;
		}
	}
	*/
}
