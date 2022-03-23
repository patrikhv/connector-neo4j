package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private String name;
    private String orgId;

    public Group(String name, String orgId) {
        this.name = name;
        this.orgId = orgId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Attribute> getAttributes() {
        Set<Attribute> attributeSet = new HashSet<>();
        attributeSet.add(AttributeBuilder.build("group_name",this.name));
        attributeSet.add(AttributeBuilder.build("organizations",this.orgId));
        return attributeSet;
    }
}
