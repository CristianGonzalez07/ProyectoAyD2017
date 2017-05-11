package trivia;

import trivia.User;
import java.util.Scanner;

/**
 * Hello Pregunta2
 *
 */
public class App
{
	private static int read(){
		Scanner sc = new Scanner(System.in);
		return sc.nextInt();
	}
	public static void main(String[] args) {
		System.out.println("Bienvenido");
		System.out.println("");
		System.out.println("Seleccione la opcion correspondiente");
		System.out.println("1- Crear Cuenta");
		System.out.println("2- Iniciar Sesion");
		System.out.println("3- Salir");
		int rta = read();
		if (rta == 1) {
			System.out.println("entre por 1");
			User.createAccount();
		}else{
			if(rta == 2){
				System.out.println("entre por 2");
				System.out.println("Seleccione la opcion correspondiente");
				System.out.println("1- Iniciar Sesion como Usuario");
				System.out.println("2- Iniciar Sesion como Administrador");
				System.out.println("3- Salir");
				rta = read();
				if(rta == 1){
					User.login("NO");
				}else{
					if(rta == 2){
						User.login("SI");
					}
				}
			}
		}
		System.out.println("Hasta Pronto........");
	}			
}