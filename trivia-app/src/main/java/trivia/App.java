package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;
//import trivia.Validations;
import java.util.Scanner;

/**
 * Hello Pregunta2
 *
 */
public class App
{
	public static String read(){
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}
	public static void main(String[] args) {
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		User p = new User();
		boolean exist = true;
		boolean valido = false;
		String s1="";
		String s2="";

		do{
			System.out.println("Ingrese Nombre de usuario");
			s1 = read();
			exist = p.exist(s1);
			valido = p.isValid(s1);
			if(exist){
				System.out.println("Nombre de usuario ya existente,ingrese otro nombre de usuario");
			}
			if (!valido){
				System.out.println("Nombre de usuario No valido,ingrese un nombre de usuario con al menos 5 caracteres");
			}
		}while((exist) || (!valido));

		System.out.println("Ingrese Contraseña");
		s2 = read();
		p.set("username",s1);
	 	p.set("password",s2);
	 	p.saveIt();
	 	Base.close();
	}
}