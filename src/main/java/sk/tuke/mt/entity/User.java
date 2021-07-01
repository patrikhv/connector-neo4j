package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class User {

    private String userName;
    private int age;
    private List<String> roles;
    private List<String> projects;

    public User() {
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
        this.roles = new LinkedList<>();
        this.projects = new LinkedList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public Set<Attribute> getAttributes() {
        // TODO automate and simplify
        Set<Attribute> attributeSet = new HashSet<>();

        attributeSet.add(AttributeBuilder.build("userName", this.userName));
        attributeSet.add(AttributeBuilder.build("age", this.age));
        if (this.roles.size() > 0){
            attributeSet.add(AttributeBuilder.build("roles",this.roles.toArray()));
        }
        if (this.projects.size() > 0){
            attributeSet.add(AttributeBuilder.build("projects",this.projects.toArray()));
        }

        return attributeSet;
    }
}
