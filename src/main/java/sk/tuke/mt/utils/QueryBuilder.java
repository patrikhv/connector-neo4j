package sk.tuke.mt.utils;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.neo4j.driver.Query;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.neo4j.driver.Values.parameters;

public class QueryBuilder {

    public static Query createQuery(ObjectClass objectClass, Set<Attribute> set){
        String type = objectClass.getObjectClassValue();
        List<String> paramsList = new ArrayList<>();
        for (Attribute attribute : set){
            String name = attribute.getName();
            String param = String.format("%s:$%s",name,name);
            paramsList.add(param);
        }

        // String i = "user_name:$user_name";
        String params = String.join(",", paramsList);
        String skeleton = String.format
                ("CREATE (x:%s {%s}) RETURN id(x)",
                type, params);  // TODO security

        Value values = createValues(set);

        return new Query(skeleton,values);
    }

    public static Query deleteQuery(ObjectClass objectClass, Uid uid){
        String type = objectClass.getObjectClassValue();
        String skeleton = String.format
                ("MATCH (x:%s) WHERE ID(x)=%s DETACH DELETE x",
                type, uid.getUidValue());
        return new Query(skeleton);
    }

    private static Value createValues(Set<Attribute> set){
        List<Object> list = new ArrayList<>();
        for(Attribute attribute: set){
            list.add(attribute.getName());
            list.add(attribute.getValue()); // TODO problem -> list of objects unpack
        }
        Object[] params = list.toArray(); // TODO check
        return parameters(params);
    }
}
