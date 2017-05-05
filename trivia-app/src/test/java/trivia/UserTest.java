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
        User user = new User();
        user.set("username", "");

        assertEquals(user.isValid(), false);
    }
}