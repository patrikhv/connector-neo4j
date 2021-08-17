package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.*;
import sk.tuke.mt.entity.Project;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;
import sk.tuke.mt.utils.QueryBuilder;

import java.util.*;

/*
    JavaDoc
Neo4j: https://neo4j.com/docs/java-reference/4.2/javadocs/index.html
connId: http://connid.tirasa.net/apidocs/1.4/index.html
 */

public class MainTest {

    public static void main(String[] args) {
        neo4jConfiguration configuration = new neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");


        neo4jConnector connector = new neo4jConnector();
        connector.init(configuration);
        Schema schema =  connector.schema();
        //connector.test();
//        User obj = new User("Denis", 33);
//        obj.getProjects().add("7");
//        obj.getRoles().add("1");
//        Role obj = new Role("student");
//
//        ObjectClass objectClass = new ObjectClass(obj.getClass().getSimpleName());
//        OperationOptionsBuilder builder = new OperationOptionsBuilder();

        // CREATEOP
//         Uid id = connector.create(objectClass,obj.getAttributes(),builder.build());
//         System.out.printf("CREATE: Id: %s%n", id.getUidValue());

        // UPDATEOP

//        Set<AttributeDelta> set = new HashSet<>();
//        AttributeDeltaBuilder attributeDeltaBuilder = new AttributeDeltaBuilder();
//        attributeDeltaBuilder.setName("roles");
//        attributeDeltaBuilder.addValueToAdd("2");
//        set.add(attributeDeltaBuilder.build());


//        Set<AttributeDelta> id = connector.updateDelta(objectClass, new Uid("8"), set,null );
        //System.out.printf("UPDATE: Id: %s%n", id.getUidValue());
        // UPDATEOP relations

//        Set<AttributeDelta> set = new HashSet<>();
//        AttributeDeltaBuilder attributeDeltaBuilder = new AttributeDeltaBuilder();
//        attributeDeltaBuilder.setName("projects");
//        attributeDeltaBuilder.addValueToRemove("4");
//        set.add(attributeDeltaBuilder.build());
//
//        ObjectClass objectClass = new ObjectClass(User.class.getSimpleName());
//        Set<AttributeDelta> id = connector.updateDelta(objectClass, new Uid("8"), set,null );

        // DELETEOP
        // connector.delete(objectClass, new Uid("1"), null);

        // SCHEMAOP
//        Schema schema =  connector.schema();
//        List<ObjectClassInfo> s = new ArrayList<>(schema.getObjectClassInfo());
//        s.forEach(System.out::println);
        Map<String,Object> m = new HashMap<>();
        m.put("operation","EQUALS");
        m.put("not",false);
        m.put("left","userName");
        m.put("right","Peter");

        QueryBuilder.getSimpleGetQuery(new ObjectClass("User"),m,null);

        connector.dispose();
    }
}
