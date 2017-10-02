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

    public void validateUniquenessOfQuestions(){
        Question question = new Question();
        question.set("description", "¿ 2x2 ?");
        question.set("category","CIENCIAS");
        question.saveIt();

        Question question2 = new Question();
        question2.set("description", "¿ 2x2 ?");
        question2.set("category","CIENCIAS");
        question2.saveIt();
        assertEquals(question2.isValid(), false);

        question.delete();        
    }

    @Test
    public void validatePresenceOfOptions(){
        Question q = new Question();
        q.set("option1", "");
        assertEquals(q.isValid(), false);
        
        q.set("option2", "");
        assertEquals(q.isValid(), false);

		q.set("option3", "");
        assertEquals(q.isValid(), false);

		q.set("option4", "");
        assertEquals(q.isValid(), false);
    }

    @Test
    public void validatecreateQuestion(){
        assertEquals(Question.createQuestion("DEPORTES","¿Cual es el mejor Equipo?",
            "Boca","River","Racing","Central"), true);
        assertEquals(Question.createQuestion("","¿Cual es el mejor Equipo?",
            "Boca","River","Racing","Central"), false);
        assertEquals(Question.createQuestion("DEPORTES","",
            "Boca","River","Racing","Central"), false);
        assertEquals(Question.createQuestion("DEPORTES","¿Cual es el mejor Equipo?",
            "","River","Racing","Central"), false);
        assertEquals(Question.createQuestion("DEPORTES","¿Cual es el mejor Equipo?",
            "Boca","","Racing","Central"), false);
        assertEquals(Question.createQuestion("DEPORTES","¿Cual es el mejor Equipo?",
            "Boca","River","","Central"), false);
        assertEquals(Question.createQuestion("DEPORTES","¿Cual es el mejor Equipo?",
            "Boca","River","Racing",""), false);
    }
}