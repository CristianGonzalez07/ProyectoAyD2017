package trivia;

import trivia.User;

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
    public void validatePresenceOfPlayers(){
        Game game = new Game();
        game.set("player1_id", "");
        assertEquals(game.isValid(), false);

        game.set("player2_id", "");
        assertEquals(game.isValid(), false);
    }
}