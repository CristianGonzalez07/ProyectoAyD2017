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
    public void validatePresenceOfPlayers(){
        Game game = new Game();
        game.set("PLAYER1", "");
        game.set("PLAYER2", "");
        assertEquals(game.isValid(), false);
    }
    
    @Test
    public void validateGame(){
        Game game = new Game();
        game.set("typeOfGame", "1PLAYER");
        game.set("Player1", "Player");
        game.set("Player2", "");
        assertEquals(game.isValid(), true);
        game.set("typeOfGame", "2PLAYER");
        game.set("player1", "");
        game.set("player2", "");
        assertEquals(game.isValid(), false);
        game.set("typeOfGame", "1PLAYER");
        game.set("Player1", "Player");
        game.set("Player2", "player2");
        assertEquals(game.isValid(), true);
        game.delete();
    }
}