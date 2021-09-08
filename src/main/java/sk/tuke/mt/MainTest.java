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
        Neo4jConfiguration configuration = new Neo4jConfiguration();
        configuration.setUri("bolt://localhost:7687");
        configuration.setUserName("neo4j");
        configuration.setPassword("qetuop");


        Neo4jConnector connector = new Neo4jConnector();
        connector.init(configuration);
        Schema schema =  connector.schema();
        System.out.println(schema.toString());
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



        //Filter filter = new (new AttributeBuilder().setName("age").addValue(1259).build());
//        Filter filterA = new ContainsFilter(new AttributeBuilder().setName("userName").addValue("Peter").build());
//        Filter filterB = new LessThanFilter(new AttributeBuilder().setName("age").addValue(1000).build());
//        Filter filterC = new AndFilter(filterA,filterB);
//        Filter filterD = new ContainsFilter(new AttributeBuilder().setName("userName").addValue("John").build());
//        Filter filterFinal = new OrFilter(filterC,filterD);
//        FilterTranslator<String> ft = connector.createFilterTranslator(new ObjectClass("User"),null);
        //QueryBuilder.getSimpleGetQuery(new ObjectClass("User"),ft.translate(equalsFilter).get(0),null);

//        connector.executeQuery(new ObjectClass("User"),ft.translate(filterFinal).get(0),new DummyResultHandler(),null);
//        connector.dispose();
    }

    public void createOp(Neo4jConnector connector){
        connector.test();
        User obj = new User("Denis", 33);
        obj.getProjects().add("7");
        obj.getRoles().add("1");
        //Role obj = new Role("student");

        ObjectClass objectClass = new ObjectClass(obj.getClass().getSimpleName());
        OperationOptionsBuilder builder = new OperationOptionsBuilder();

        // CREATEOP
         Uid id = connector.create(objectClass,obj.getAttributes(),builder.build());
         System.out.printf("CREATE: Id: %s%n", id.getUidValue());
    }
}
