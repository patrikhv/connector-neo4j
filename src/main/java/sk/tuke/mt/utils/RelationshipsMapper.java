package sk.tuke.mt.utils;

import org.identityconnectors.framework.common.objects.Attribute;

import java.util.LinkedList;
import java.util.List;


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

}
