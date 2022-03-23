package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import sk.tuke.mt.entity.Group;
import sk.tuke.mt.entity.Organization;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;

import java.util.LinkedList;
import java.util.List;

public class CreataOrgRolGro {

    public static void main(String[] args) {
        Neo4jConfiguration configuration = new Neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");


        Neo4jConnector connector = new Neo4jConnector();
        connector.init(configuration);
        connector.test();
        schemaOp(connector);

        Organization organization = new Organization("The Great Company");
        List<String> roles = new LinkedList<>(List.of("K8s System", "CEO", "App Service Reporter", "Developer",
                "Manager", "Reporter", "Tester"));

        List<String> groups = new LinkedList<>(List.of("K8s System", "Default"));

        ObjectClass objectClass = new ObjectClass(organization.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder();
        Uid idorg = connector.create(objectClass,organization.getAttributes(),builder.build());
        System.out.printf("CREATE: Id: %s%n", idorg.getUidValue());

        for (String rolename: roles){
            Role role = new Role(rolename);
            objectClass = new ObjectClass(role.getClass().getSimpleName());
            builder = new OperationOptionsBuilder();
            Uid id = connector.create(objectClass, role.getAttributes(), builder.build());
            System.out.printf("CREATE: Id: %s%n", id.getUidValue());
        }

        for (String groupname: groups){
            Group group = new Group(groupname, idorg.getUidValue());
            objectClass = new ObjectClass(group.getClass().getSimpleName());
            builder = new OperationOptionsBuilder();
            Uid id = connector.create(objectClass, group.getAttributes(), builder.build());
            System.out.printf("CREATE: Id: %s%n", id.getUidValue());
        }



    }

    public static void schemaOp(Neo4jConnector connector){
        Schema schema = connector.schema();
        System.out.println(schema.toString());
    }
}
