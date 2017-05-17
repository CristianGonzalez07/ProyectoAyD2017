package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import trivia.Question;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Game{

	private static int random(int init,int end) {		//retorna un nro aleatorio en el intervalo tomado como parametro(considerando extremos)
		Random  rnd = new Random();
		return (int)(rnd.nextDouble() * end + init);
	}

	public static void play(){							//permite al usuario respoder una pregunta	
		int n = random(1,5); 
		System.out.println("El resultado de girar la ruleta fue: "+n);
		String cat = getCat(n);
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		List<Question> questions =  Question.getQuestionsByCategory(cat);
		n = random(0,questions.size()-1);
		System.out.println("el nro de pregunta fue: "+n);
		showQuestion(questions.get(n));
		Base.close();
	}

	private static String getCat(int n){				//retorna el string correspondiente al nro de cat tomado como parametro
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
		System.out.println("Ingrese su rta: ");
		String rta = read();
		if(q.get("correct").equals(rta)){
			System.out.println("Respuesta correcta");
		}else{
			System.out.println("Respuesta incorrecta");
		}
	}

	private static Boolean getAnswer(Question q, String r){	//dada una pregunta y una respuesta determina si la rta es correcta o no
		if(q.get("correct").equals(r)){
			return true;
		}else{
			return false;
		}
	}

	private static String read(){												//retorna un string tomado desde el teclado
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}
}
