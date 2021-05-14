package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.*;
import sk.tuke.mt.entity.User;

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

        connector.test();

        User obj = new User("Juraj", 12);
        //Role obj = new Role("student");


        ObjectClass objectClass = new ObjectClass(obj.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder(); // TODO fix error caused by builder

        // CREATEOP
        Uid id = connector.create(objectClass,obj.getAttributes(),builder.build());
        System.out.printf("CREATE: Id: %s%n", id.getUidValue());

        // UPDATEOP
        // Uid id = connector.update(objectClass, new Uid("5"), first.getAttributes(), null );
        //System.out.printf("UPDATE: Id: %s%n", id.getUidValue());

        // DELETEOP
        // connector.delete(objectClass, new Uid("1"), null);


        /*java.lang.ClassNotFoundException: org.identityconnectors.framework.impl.serializer.ObjectSerializerFactoryImpl*/

        // SCHEMAOP
        //Schema schema =  connector.schema();
        //List<ObjectClassInfo> s = new ArrayList<>(schema.getObjectClassInfo());
        //s.forEach(System.out::println);



        connector.dispose();
    }
}
