package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.HashSet;
import java.util.Set;

public class Role {
    private String roleName;


    public Role(){
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Attribute> getAttributes() {
        // TODO automate and simplify
        Set<Attribute> attributeSet = new HashSet<>();

        AttributeBuilder builder = new AttributeBuilder();
        builder.setName("roleName");
        builder.addValue(this.roleName);

        attributeSet.add(builder.build());
        return attributeSet;
    }
}
