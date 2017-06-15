package trivia;

import trivia.Game;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameTest{
    
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
    public void validatePresenceOfUsers(){
        Game game = new Game();
        game.set("user", "");
        assertEquals(game.isValid(), false);
    }

    @Test
    public void validateDescription(){
        Game game = new Game();
        game.set("description", "");
        assertEquals(game.isValid(), false);
    }
}