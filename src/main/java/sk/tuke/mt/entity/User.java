package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.*;

public class User {

    private String userName;
    private List<String> roles;
    private List<String> groups;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
        this.roles = new LinkedList<>();
        this.groups = new LinkedList<>();
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    public Set<Attribute> getAttributes() {
        Set<Attribute> attributeSet = new HashSet<>();

        attributeSet.add(AttributeBuilder.build("user_name", this.userName));
        if (this.roles.size() > 0){
            attributeSet.add(AttributeBuilder.build("roles",this.roles.toArray()));
        }
        if (this.groups.size() > 0){
            attributeSet.add(AttributeBuilder.build("groups",this.groups.toArray()));
        }


        return attributeSet;
    }
}
