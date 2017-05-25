package trivia;

import trivia.Question;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class QuestionTest{
    
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
    public void validatePresenceOfDescription(){
        Question q = new Question();
        q.set("description", "");
        assertEquals(q.isValid(), false);
    }

    @Test
    public void validatePresenceOfCategory(){
        Question q = new Question();
        q.set("category", "");
        assertEquals(q.isValid(), false);
    }

    @Test
    public void validatePresenseOfOptions(){
        Question q = new Question();
        q.set("option1", "");
        assertEquals(q.isValid(), false);
        
        q.set("option2", "");
        assertEquals(q.isValid(), false);

		q.set("option1", "");
        assertEquals(q.isValid(), false);

		q.set("option1", "");
        assertEquals(q.isValid(), false);

    }


}