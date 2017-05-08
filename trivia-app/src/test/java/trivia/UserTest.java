package trivia;

import trivia.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest{
    @Test
    public void validatePrecenseOfUsernames(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        User user = new User();
        user.set("username", "");

        assertEquals(user.isValid(), false);
        Base.close();
    }
}