package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import sk.tuke.mt.entity.User;

import java.util.LinkedList;

public class CreateTestMain {

    public static void main(String[] args) {
        neo4jConfiguration configuration = new neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");


        neo4jConnector connector = new neo4jConnector();
        connector.init(configuration);

        //ROLE
//        Role obj = new Role("student");

        // USER
        User obj = new User("Jan", 54);
        obj.setRolesId(new LinkedList<>());
        obj.getRolesId().add("1");
        obj.getRolesId().add("2");


        ObjectClass objectClass = new ObjectClass(obj.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder();

        // CREATEOP
        Uid id = connector.create(objectClass,obj.getAttributes(),builder.build());
        System.out.printf("CREATE: Id: %s%n", id.getUidValue());



        connector.dispose();
    }
}
