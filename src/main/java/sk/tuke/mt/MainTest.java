package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import sk.tuke.mt.entity.User;


public class MainTest {

    public static void main(String[] args) {
        neo4jConfiguration configuration = new neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");


        neo4jConnector connector = new neo4jConnector();
        connector.init(configuration);

        connector.test();

        User first = new User("Martin", 99);

        ObjectClass objectClass = new ObjectClass(first.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder(); // TODO fix error caused by builder

        // Uid id = connector.create(objectClass,first.getAttributes(),null);
        // Uid id = connector.update(objectClass, new Uid("5"), first.getAttributes(), null );
        // connector.delete(objectClass, new Uid("1"), null);

        //System.out.printf("Id: %s%n", id.getUidValue());





        connector.dispose();
    }
}
