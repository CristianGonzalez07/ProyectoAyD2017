package trivia;

import trivia.Question;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class invitationTest{
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
        invitation n = new invitation();
        n.set("origin", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfDestination(){
        invitation n = new invitation();
        n.set("destination", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfAccepted(){
        invitation n = new invitation();
        n.set("accepted", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validate_createinvitation(){
        String origin = "Cristian"; 
        String destination = "Seba";
   
        assertEquals(invitation.createinvitation(origin,destination,content),true);
        origin = "";
        assertEquals(invitation.createinvitation(origin,destination,content),false);
    }
}