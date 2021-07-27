package sk.tuke.mt.utils;

import org.identityconnectors.framework.common.objects.Attribute;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RelationshipsMapper {

    public static List<Relationship> getRelationships() {
        List<Relationship> relationships = new LinkedList<>();

        Relationship isMemberOf = new Relationship("User","Role",
                "IS_MEMBER_OF","roles");
        Relationship isWorkingOn = new Relationship("User","Project",
                "IS_WORKING_ON","projects");
        relationships.add(isMemberOf);
        relationships.add(isWorkingOn);
        return relationships;
    }

    public static Relationship getRelationship(Attribute attribute){
        for (Relationship relationship : getRelationships()){
            if (relationship.getVirtualAttributeName().equals(attribute.getName())){
                return relationship;
            }
        }
        return null;
    }

    public static void getRelationshipsFromSchema(List<Record> records){
        System.out.println(records.get(0).get(0));
        List<Map> relationships = new LinkedList<>();
        Map<String,Object> data = records.get(0).get(0).asMap();
        for (String key: data.keySet()){
            Map<String,Object> object = (Map) data.get(key);
            if ("node".equals(object.get("type"))){
                relationships.add(object);
            }
        }
        for(Map relation: relationships){
            Map map = (Map) relation.get("relationships");
            for (Object s: map.keySet()){
                System.out.println(map.get(s));
            }
        }

        //meno, TODO prop, smer, kam
        //System.out.println(relationships.get(0).get("relationships"));

    }
}
