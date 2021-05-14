package sk.tuke.mt.utils;

import org.identityconnectors.framework.common.objects.*;
import org.neo4j.driver.Record;
import sk.tuke.mt.neo4jConnector;

import java.util.*;
import java.util.stream.Collectors;

public class SchemaHelper {

    public static Schema getSchema(List<Record> records){
        List<String> nodeNames = getNodeNames(records); // OK
        //nodeNames.forEach(System.out::println);
        HashMap<String, List<PropertyMapper>> properties = getProperties(nodeNames, records);
        System.out.println("******");
        for (String nodeName : nodeNames){
            properties.get(nodeName).forEach(System.out::println);
        }

        return generateSchema(properties);
    }

    // Help: http://connid.tirasa.net/apidocs/1.4/org/identityconnectors/framework/common/objects/SchemaBuilder.html
    private static Schema generateSchema(HashMap<String, List<PropertyMapper>> properties){
        SchemaBuilder builder = new SchemaBuilder(neo4jConnector.class);
        properties.forEach((name,list) -> {
                ObjectClassInfoBuilder objectClassBuilder = new ObjectClassInfoBuilder();
                objectClassBuilder.setType(name);
                list.forEach(property -> {
                    AttributeInfoBuilder uidAib = new AttributeInfoBuilder(Uid.NAME);
                    uidAib.setName(property.getPropertyName());
                    uidAib.setType(property.getPropertyJavaType()); // TODO fix "Attribute type 'interface java.util.List' is not supported."
                    uidAib.setRequired(property.isMandatory());
                    objectClassBuilder.addAttributeInfo(uidAib.build());
                });
                builder.defineObjectClass(objectClassBuilder.build());
        });
        return builder.build();
    }

    private static List<String> getNodeNames(List<Record> records){
        List<String> nodeNames = new LinkedList<>();
        for (Record record : records){
            String nodeName = record.get("nodeType").asString();
            nodeNames.add(cleanNodeName(nodeName));
        }
        return removeDupList(nodeNames, false);
    }

    private static HashMap<String, List<PropertyMapper>> getProperties(List<String> nodeNames ,List<Record> records){
        HashMap<String, List<PropertyMapper>> nodesProperties = new HashMap<>();
        for (String nodeName : nodeNames){
            List<Record> filteredRecords = records
                    .stream()
                    .filter(record -> cleanNodeName(record.get("nodeType").asString()).equals(nodeName))
                    .collect(Collectors.toList());;
            System.out.println("--- " + nodeName + "---");
            filteredRecords.forEach(System.out::println);
            System.out.println("--- end " + nodeName + "end ---");
            List<PropertyMapper> propertyMappers = new ArrayList<>();
            for (Record record : filteredRecords){
                String name = record.get("propertyName").asString();
                String type = record.get("propertyTypes").asList().get(0).toString(); // TODO What if Neo has multiple types in one property
                boolean mandatory = record.get("mandatory").asBoolean();
                propertyMappers.add(new PropertyMapper(name,type,mandatory));
            }
            nodesProperties.put(nodeName,propertyMappers);
        }

        return nodesProperties;
    }

    private static List<String> removeDupList(List<String>list, boolean ignoreCase){
        Set<String> set = (ignoreCase?new TreeSet<String>(String.CASE_INSENSITIVE_ORDER):new LinkedHashSet<String>());
        set.addAll(list);
        return new ArrayList<String>(set);
    }

    private static String cleanNodeName(String nodeName){
        return nodeName.replace(":","").replace("`","");
    }


}
