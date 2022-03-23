package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import sk.tuke.mt.entity.User;

import java.util.LinkedList;
import java.util.List;

public class CreateUsers {
    public static void main(String[] args) {
        Neo4jConfiguration configuration = new Neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");

        List<String> users = new LinkedList<>(List.of("K8s System user", "K8s Service Account","Adewale Ross", "Andrew Trump", "Ashley Bush",
                "Elisabeth Red", "Felix Bloom", "Jeffrey Norton", "Jill Musk", "John Frusciante", "Nigel Grohl", "Rosemary Torsh"));

        Neo4jConnector connector = new Neo4jConnector();
        connector.init(configuration);
        connector.test();
        schemaOp(connector);

        User obj = new User("K8s Service Account");
        obj.setRoles(List.of("5"));
        obj.setGroups(List.of("11"));

        ObjectClass objectClass = new ObjectClass(obj.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder();

        Uid id = connector.create(objectClass,obj.getAttributes(),builder.build());
        System.out.printf("CREATE: Id: %s%n", id.getUidValue());

        connector.dispose();

    }

    public static void schemaOp(Neo4jConnector connector){
        Schema schema = connector.schema();
    }
}
