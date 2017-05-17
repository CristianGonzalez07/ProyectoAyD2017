package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import java.util.List;
import java.util.Scanner;

public class Question extends Model{

  /*OBTENER CATEGORIA INGRESANDO UN VALOR*/
  private static String getCategory(){
    int category;
    System.out.println("Ingrese Categoria");

    //Listar categorias disponibles
    System.out.println("CIENCIAS");
    System.out.println("DEPORTE");
    System.out.println("ENTRETENIMIENTO");
    System.out.println("GEOGRAFIA");
    System.out.println("HISTORIA");

    //Read category
    category = readInt();
    String res = "";
    switch (category) {
      case 1: 
        res = "CIENCIAS";
      break;

      case 2: 
        res = "DEPORTE";
      break;
      
      case 3:
        res =  "ENTRETENIMIENTO";
      break;
      
      case 4:
        res = "GEOGRAFIA";
      break;
      
      case 5:
         res =  "HISTORIA";
      break;
    
    }//End switch
    return res;
  } //End getCategory

  private static void showQuestions(List<Question> questions){
    for (int i=0;i<questions.size();i++){
      System.out.println((questions.get(i)).get("question"));    
    }
  }

  /* RETORNA EL LISTADO DE PREGUNTAS CON UN MISMO TAG EN UNA CATEGORIA DADA*/
  public static List<Question> getQuestionsByTag(String nTag,String nCat){
 	  List<Question> questions = where("tag = '"+nTag+"' and category = '"+nCat+"'");
    return questions;
  }

  public static List<Question> getQuestionsByCategory(String nCat){
    List<Question> questions = where("category = '"+nCat+"'");
    return questions;
  }

  public static void loadQuestion(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    String setQuestion;
    String opc;
    Question q = new Question();
    System.out.println("Ingrese el TAG de la pregunta que desea cargar");
    String tag = read();
    String cat = getCategory();
    List<Question> listaTag = getQuestionsByTag(tag,cat);

    if (listaTag.size() != 0){  //Lista todas las preguntas con ese tag para verificar la existencia de la pregunta a ingresar
       showQuestions(listaTag);
    }    
    q.set("tag",tag);
    q.set("category",cat);
    
    System.out.println("Ingrese la pregunta que desea cargar");
    setQuestion = read();
    if(!q.isValidQ(setQuestion,3)){  //el tamaño min de preg es 3 pues la menor pregunta q se puede hacer es una operacion matematica basica ej 4+4
      System.out.println("No se puede ingresar una pregunta con menos de 3 caracteres");
    }else{
      //por el momento asumimos q las opciones son todas validas(mas adelante las opciones tendran validacion de no ser vacias que actua sobre el formulario de carga)
      q.set("question",setQuestion);
      System.out.println("Cargue opcion 1");
      opc = read();
      q.set("option1",opc);
      System.out.println("Cargue opcion 2");
      opc = read();
      q.set("option2",opc);
      System.out.println("Cargue opcion 3");
      opc = read();
      q.set("option3",opc);
      System.out.println("Cargue opcion 4");
      opc = read();
      q.set("option4",opc);
      System.out.println("Opcion Correcta 1,2,3 o 4?");
      opc = read();
      q.saveIt();
    }
    Base.close();
  } //End loadQuestion

  /*ELIMINA UNA PREGUNTA DEL SISTEMA*/
  public static void deleteQuestion(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
    List<Question> listaDisponible;
    System.out.println("Ingrese la categoria de la pregunta la cual desea borrar");
    String cat = getCategory();
    System.out.println("Desea filtrar por tag?: 1 si 2 no");
    String rta = read();
    if(rta.equals('1')){
      System.out.println("Ingrese el tag");
      String tag = read();
      listaDisponible = getQuestionsByTag(cat,tag); //Retorna la lista de preguntas posibles a eliminar
    }else{
      listaDisponible = getQuestionsByCategory(cat); //Retorna la lista de preguntas posibles a eliminar
    }
    System.out.println("Las preguntas disponibles para eliminar son las siguientes: " );
    showQuestions(listaDisponible);
    System.out.println("ingrese el numero de la pregunta a eliminar.Si no desea eliminar ninguna ingrese cero");
    int num = readInt();
    (listaDisponible.get(num)).delete();
    Base.close();
  } //End deleteQuestionUser.login("NO")

  /*RETORNA UN STRING INGRESADO DESDE EL TECLADO*/
  private static String read(){
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	} //End class read

    private static int readInt(){
    Scanner sc = new Scanner(System.in);
    return sc.nextInt();
  } //End class read

  /* RETORNA TRUE SI LA CADENA TIENE COMO MINIMO LA LONGITUD DADA*/
  private static Boolean isValidQ(String s,int n){
		if(s.length()<n){
			return false;
		}else{
			return true;
		}
	} //End class isValid

} //End class Question
