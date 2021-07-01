package sk.tuke.mt.utils;

public class Relationship {

    private String fromObjectName;
    private String toObjectName;
    private String relationshipName;
    private String virtualAttributeName;

    public Relationship(String fromObjectName, String toObjectName, String relationshipName, String virtualAttributeName) {
        this.fromObjectName = fromObjectName;
        this.toObjectName = toObjectName;
        this.relationshipName = relationshipName;
        this.virtualAttributeName = virtualAttributeName;
    }

    public String getFromObjectName() {
        return fromObjectName;
    }

    public void setFromObjectName(String fromObjectName) {
        this.fromObjectName = fromObjectName;
    }

    public String getToObjectName() {
        return toObjectName;
    }

    public void setToObjectName(String toObjectName) {
        this.toObjectName = toObjectName;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getVirtualAttributeName() {
        return virtualAttributeName;
    }

    public void setVirtualAttributeName(String virtualAttributeName) {
        this.virtualAttributeName = virtualAttributeName;
    }
}
