package sk.tuke.mt.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.neo4j.driver.Record;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RelationshipsMapper {

    public static List<Relationship> relationshipList = new LinkedList<>();


    public static List<Relationship> getRelationships() {
        return relationshipList;
    }

    public static Relationship getRelationship(Attribute attribute){
        for (Relationship relationship : getRelationships()){
            if (relationship.getVirtualAttributeName().equals(attribute.getName())){
                return relationship;
            }
        }
        return null;
    }

    public static void addRelationship(String from, String to, String name){
        String virtualAttribute = to.toLowerCase() + "s";
        Relationship relationship = new Relationship(from,to,name,virtualAttribute);
        relationshipList.add(relationship);
    }

    public static void getRelationshipsFromSchema(List<Record> records){
        relationshipList = new LinkedList<>();
        // TODO rel properties
        JsonObject jsonObject = new JsonParser().parse(records.get(0).get(0).toString()).getAsJsonObject();
        List<String>
                elements = getKeys(jsonObject);
        for (String element: elements){
            JsonObject elementJson = jsonObject.get(element).getAsJsonObject();
            String type = elementJson.get("type").getAsString();
            if ("node".equals(type)){
                JsonObject relationships = jsonObject.get(element).getAsJsonObject().get("relationships").getAsJsonObject();
                List<String> relKeys = getKeys(relationships);
                for (String relKey: relKeys){
                    // TODO CHECK Multiple nodes in rel
                    String node2 = relationships.get(relKey).getAsJsonObject().get("labels").getAsString();
                    String direction = relationships.get(relKey).getAsJsonObject().get("direction").getAsString();
                    //TODO check also IN, OUT
                    if ("out".equals(direction)){
                        addRelationship(element,node2,relKey);
                    }
                }
            }
        }

    }
    public static List<String> getKeys(JsonObject jsonObject){
        List<String> keys = new LinkedList<>();
        for (Map.Entry<String, JsonElement> s :jsonObject.entrySet()){
            keys.add(s.getKey().toString());
        }
        return keys;
    }
}
