package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.HashSet;
import java.util.Set;

public class Organization {

    private String name;


    public Organization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Attribute> getAttributes() {
        // TODO automate and simplify
        Set<Attribute> attributeSet = new HashSet<>();

        AttributeBuilder builder = new AttributeBuilder();
        builder.setName("organization_name");
        builder.addValue(this.name);

        attributeSet.add(builder.build());
        return attributeSet;
    }
}
