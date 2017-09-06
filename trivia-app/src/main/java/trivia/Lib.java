package trivia;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import trivia.User;
import trivia.Question;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static spark.Spark.*;

public class Lib{

	public static int random(int init,int end) {
		Random  rnd = new Random();
		return (int)(rnd.nextDouble() * end + init);
	}

}