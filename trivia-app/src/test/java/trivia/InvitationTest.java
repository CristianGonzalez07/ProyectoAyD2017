package trivia;

import trivia.Question;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InvitationTest{
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
        Invitation n = new Invitation();
        n.set("origin", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfDestination(){
        Invitation n = new Invitation();
        n.set("destination", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validatePresenceOfAccepted(){
        Invitation n = new Invitation();
        n.set("accepted", "");
        assertEquals(n.isValid(), false);
    }

    @Test
    public void validate_createinvitation(){
        String origin = "Cristian"; 
        String destination = "Seba";
   
        assertEquals(Invitation.createInvitation(origin,destination),true);
        origin = "";
        assertEquals(Invitation.createInvitation(origin,destination),false);
    }
}