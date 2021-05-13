package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;


public class MainTest {

    public static void main(String[] args) {
        neo4jConfiguration configuration = new neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");


        neo4jConnector connector = new neo4jConnector();
        connector.init(configuration);

        //connector.test();

        //User obj = new User("Ondrej", 57);
        //Role obj = new Role("director");

        /*
        ObjectClass objectClass = new ObjectClass(obj.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder(); // TODO fix error caused by builder
        Uid id = connector.create(objectClass,obj.getAttributes(),null);*/


        // Uid id = connector.update(objectClass, new Uid("5"), first.getAttributes(), null );
        // connector.delete(objectClass, new Uid("1"), null);

        //System.out.printf("Id: %s%n", id.getUidValue());

        connector.schema();



        connector.dispose();
    }
}
