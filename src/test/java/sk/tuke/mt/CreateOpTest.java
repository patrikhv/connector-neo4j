package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.tuke.mt.entity.Project;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;


public class CreateOpTest {

    private neo4jConnector connector;

    @BeforeClass
    public void setUp(){
        neo4jConfiguration configuration = new neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");

        this.connector = new neo4jConnector();
        connector.init(configuration);
    }

    @Test
    public void createUserTest(){
        Role student = new Role("student");
        Role teacher = new Role("teacher");

        Project thesis = new Project("thesis");

        OperationOptionsBuilder builder = new OperationOptionsBuilder();

        Uid studentId = connector.create(new ObjectClass(student.getClass().getSimpleName()),student.getAttributes(),builder.build());
        Uid teacherId = connector.create(new ObjectClass(teacher.getClass().getSimpleName()),teacher.getAttributes(),builder.build());

        Uid thesisId = connector.create(new ObjectClass(thesis.getClass().getSimpleName()),thesis.getAttributes(),builder.build());


        User john = new User("John", 54);
        john.getRoles().add(studentId.getUidValue());
        john.getRoles().add(teacherId.getUidValue());
        john.getProjects().add(thesisId.getUidValue());

        Uid johnId = connector.create(new ObjectClass(john.getClass().getSimpleName()),john.getAttributes(),builder.build());

    }
}
