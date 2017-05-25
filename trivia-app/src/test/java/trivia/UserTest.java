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
         user.set("username", "Cristian");
         user.set("password","1234");
         user.saveIt();

         User user2 = new User();
         user.set("username", "Cristian");
         user.set("password","12345");

         assertEquals(user2.isValid(), false);

        user.delete();        
    }
}