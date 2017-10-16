package trivia;

import trivia.Question;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NotificationTest{
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
    public void validatePresenceOfOrigin(){
        Notification n = new Notification();
        q.set("origin", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfDestination(){
        Notification n = new Notification();
        q.set("destination", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfOrigin(){
        Notification n = new Notification();
        q.set("content", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfOrigin(){
        Notification n = new Notification();
        q.set("viewed", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfOrigin(){
        Notification n = new Notification();
        q.set("accepted", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validate_createNotification(){
        String origin = "Cristian"; 
        String destination = "Seba"
        String content = "Prueba"
   
        assertEquals(Notification.createNotification(origin,destination,content),true);
        origin = "";
        assertEquals(Notification.createNotification(origin,destination,content),false);
    }
}