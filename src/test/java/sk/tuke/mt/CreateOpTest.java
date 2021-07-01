package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;

import java.util.LinkedList;

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
    public void createUserRoleTest(){
        Role student = new Role("student");
        Role teacher = new Role("teacher");

        OperationOptionsBuilder builder = new OperationOptionsBuilder();

        Uid studentId = connector.create(new ObjectClass(student.getClass().getSimpleName()),student.getAttributes(),builder.build());
        Uid teacherId = connector.create(new ObjectClass(teacher.getClass().getSimpleName()),teacher.getAttributes(),builder.build());

        User john = new User("John", 54);
        john.setRolesId(new LinkedList<>());
        john.getRolesId().add(studentId.getUidValue());
        john.getRolesId().add(teacherId.getUidValue());

        Uid johnId = connector.create(new ObjectClass(john.getClass().getSimpleName()),john.getAttributes(),builder.build());

    }
}
