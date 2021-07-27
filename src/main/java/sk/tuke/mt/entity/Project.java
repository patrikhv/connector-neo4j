package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.HashSet;
import java.util.Set;

public class Project {

    private String projectName;

    public Project(){}

    public Project(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Set<Attribute> getAttributes() {
        // TODO automate and simplify
        Set<Attribute> attributeSet = new HashSet<>();

        AttributeBuilder builder = new AttributeBuilder();
        builder.setName("projectName");
        builder.addValue(this.projectName);

        attributeSet.add(builder.build());
        return attributeSet;
    }
}
