package trivia;

import trivia.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest{
    
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Base.openTransaction();
    }

    @After
    public void after(){
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void validatePresenceOfUsernames(){
        User user = new User();
        user.set("username", "");
        assertEquals(user.isValid(), false);
    }

    @Test
    public void validatePassword(){
        User user = new User();
        user.set("password", "");
        assertEquals(user.isValid(), false);
    }

     @Test
     public void validateUniquenessOfUsernames(){
        User user = new User();
        user.set("username", "Prueba");
        user.set("password","1234");
        user.saveIt();

        User user2 = new User();
        user.set("username", "Prueba");
        user.set("password","12345");

        assertEquals(user2.isValid(), false);

        user.delete();        
    }

    @Test
    public void validateRegister(){
        String username = "";
        String pass = "";
        assertEquals(User.register(username,pass),2);

        username = "Prueba";
        pass = "1234";
        User user = new User();
        user.set("username", username);
        user.set("password",pass);
        user.saveIt();
        assertEquals(User.register(username,pass),1);

        user.delete(); 
        username = "Prueba";
        pass = "1234";
        assertEquals(User.register(username,pass),3);
    }

    @Test
    public void validateFunctionValidateLogin(){
        String username = "";
        String pass = "";
        String perms = "NO";  
        assertEquals(User.validateLogin(username,pass,perms),false);
       
        perms = "YES";
        assertEquals(User.validateLogin(username,pass,perms),false);
        
        username = "Prueba";
        pass = "1234";
        User user = new User();
        user.set("username", username);
        user.set("password",pass);
        user.saveIt();
        assertEquals(User.validateLogin(username,pass,perms),false);
       
        user.delete();
        assertEquals(User.validateLogin("CGonzalez","pregunta2","YES"),true);
    }
}