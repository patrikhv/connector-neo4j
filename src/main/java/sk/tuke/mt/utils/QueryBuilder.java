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
        String paramsNames = getParams(set);

        String skeleton = String.format
                ("CREATE (x:%s {%s})\n" +
                 "RETURN id(x)",
                type, paramsNames);

        Value paramsValues = createValues(set);

        return new Query(skeleton,paramsValues);
    }

    private static String getParams(Set<Attribute> set){
        List<String> paramsList = new ArrayList<>();
        for (Attribute attribute : set){
            if (isVirtualAttribute(attribute)){
                continue;
            }
            String name = attribute.getName();
            String param = String.format("%s:$%s",name,name);
            paramsList.add(param);
        }
        return String.join(",", paramsList);
    }

    public static boolean isVirtualAttribute(Attribute attribute){
        List<Relationship> relationships = RelationshipsMapper.getRelationships();
        for (Relationship relationship: relationships){
            if (relationship.getVirtualAttributeName().equals(attribute.getName())){
                return true;
            }
        }
        return false;
    }

    public static Query createRelationshipQuery(String uid, String to, Relationship relationship, List<Object> params) {
        String skeleton = String.format
                                ("MATCH (u:%s),(r:%s)\n" +
                                "WHERE ID(u)=%s AND ID(r)=%s\n" +
                                "CREATE (u)-[s:%s]->(r)\n"+
                                "RETURN ID(s)",
                                relationship.getFromObjectName(),relationship.getToObjectName(), uid, to, relationship.getRelationshipName());
        System.out.println(new Query(skeleton).toString());
        return new Query(skeleton);
    }


    public static Query deleteQuery(ObjectClass objectClass, Uid uid){
        String type = objectClass.getObjectClassValue();
        String skeleton = String.format
                ("MATCH (x:%s)\n" +
                 "WHERE ID(x)=%s\n" +
                 "DETACH DELETE x",
                type, uid.getUidValue());
        return new Query(skeleton);
    }


    public static Query updateQuery(ObjectClass objectClass,Uid uid,Set<Attribute> set){
        String type = objectClass.getObjectClassValue();
        List<String> paramsToUpdate = new ArrayList<>();
        for (Attribute attribute : set){
            String name = attribute.getName();
            String param = String.format("x.%s = $%s",name,name);
            paramsToUpdate.add(param);
        }

        String params = String.join(",", paramsToUpdate);
        String skeleton = String.format
                ("MATCH (x:%s)\n" +
                 "WHERE ID(x)=%s\n" +
                 "SET %s\n" +
                 "RETURN ID(x)",
                 type, uid.getUidValue(), params);

        Value values = createValues(set);

        return new Query(skeleton,values);
    }

    public static Query schemaQueryAll(){
        String skeleton = "CALL db.schema.nodeTypeProperties";
        return new Query(skeleton);
    }

    public static Query schemaQueryRel(){
        String skeleton = "CALL apoc.meta.schema";
        return new Query(skeleton);
    }

    private static Value createValues(Set<Attribute> set){
        List<Object> list = new ArrayList<>();
        for(Attribute attribute: set){
            if (isVirtualAttribute(attribute)){
                continue;
            }
            list.add(attribute.getName());
            if (attribute.getValue().size() == 1){
                list.add(attribute.getValue().get(0));
            }else{
                list.add(attribute.getValue()); // TODO problem -> list of objects unpack
            }

        }
        Object[] params = list.toArray(); // TODO check

        return parameters(params);
    }
}
