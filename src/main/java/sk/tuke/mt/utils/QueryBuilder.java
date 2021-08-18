package sk.tuke.mt.utils;

import org.identityconnectors.framework.common.objects.*;
import org.neo4j.driver.Query;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static boolean isVirtualAttribute(AttributeDelta attribute){
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

    public static Query deleteRelationshipQuery(String uid, String to, Relationship relationship) {
        String skeleton = String.format
                ("MATCH (u:%s)-[r:%s]->(v:%s)\n" +
                                "WHERE ID(u)=%s AND ID(v)=%s\n" +
                                "DELETE r",
                        relationship.getFromObjectName(),relationship.getRelationshipName(),relationship.getToObjectName(),
                        uid, to);
        System.out.println(new Query(skeleton).toString());
        return new Query(skeleton);
    }

    public static Query deleteAllRelationshipQuery(String uid, Relationship relationship) {
        String skeleton = String.format
                ("MATCH (u:%s)-[r:%s]->(v:%s)\n" +
                                "WHERE ID(u)=%s" +
                                "DELETE r",
                        relationship.getFromObjectName(), relationship.getRelationshipName(), relationship.getToObjectName(),
                        uid);
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

    public static Query updateDeltaQuery(ObjectClass objectClass,Uid uid,Set<AttributeDelta> set) {
        // SET n.id = coalesce(n.id, []) + n.additionalId
        String type = objectClass.getObjectClassValue();
        List<String> paramsToUpdate = new ArrayList<>();
        for (AttributeDelta attribute : set) {
            if (isVirtualAttribute(attribute)){
                continue;
            }
            String name = attribute.getName();
            // TODO check and test values, especially null values
            if (attribute.getValuesToAdd() != null) {
                String param = String.format("x.%s = coalesce(x.%s,[]) + $%s", name, name, name);
                paramsToUpdate.add(param);
            }
            if (attribute.getValuesToRemove() != null) {
                String param = String.format("x.%s = [z IN x.%s WHERE z <> $%s]", name, name, name);
                paramsToUpdate.add(param);
            }
            if (attribute.getValuesToReplace() != null) {
                String param = String.format("x.%s = $%s", name, name);
                paramsToUpdate.add(param);
            }

        }

        // empty query
        if (paramsToUpdate.size() == 0){
            return null;
        }
        String params = String.join(",", paramsToUpdate);
        String skeleton = String.format
                ("MATCH (x:%s)\n" +
                                "WHERE ID(x)=%s\n" +
                                "SET %s\n" +
                                "RETURN ID(x)",
                        type, uid.getUidValue(), params);

        Value values = createValuesFromDelta(set);
        System.out.println(new Query(skeleton,values).toString());
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

    private static Value createValuesFromDelta(Set<AttributeDelta> set){
        List<Object> list = new ArrayList<>();
        for(AttributeDelta attribute: set){
            if (isVirtualAttribute(attribute)){
                continue;
            }
            list.add(attribute.getName());
            if (attribute.getValuesToReplace() != null) {
                if (attribute.getValuesToReplace().size() == 1) {
                    list.add(attribute.getValuesToReplace().get(0));
                } else {
                    list.add(attribute.getValuesToReplace()); // TODO problem -> list of objects unpack
                }
            }
            if (attribute.getValuesToAdd() != null) {
                if (attribute.getValuesToAdd().size() == 1) {
                    list.add(attribute.getValuesToAdd().get(0));
                } else {
                    list.add(attribute.getValuesToAdd()); // TODO problem -> list of objects unpack
                }
            }
            if (attribute.getValuesToRemove() != null) {
                if (attribute.getValuesToRemove().size() == 1) {
                    list.add(attribute.getValuesToRemove().get(0));
                } else {
                    list.add(attribute.getValuesToRemove()); // TODO problem -> list of objects unpack
                }
            }

        }
        Object[] params = list.toArray(); // TODO check

        return parameters(params);
    }

    public static Query getSimpleGetQuery(ObjectClass objectClass, String subQuery, OperationOptions operationOptions){
        String type = objectClass.getObjectClassValue();
        String skeleton = String.format(
                """
                        MATCH (n:%s)
                        WHERE %s
                        RETURN n
                        """,type,subQuery);
            //System.out.println(new Query(skeleton).toString());
            return new Query(skeleton);
    }
}
