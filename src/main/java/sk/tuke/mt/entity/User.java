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
    private List<String> rolesId;

    public User() {
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
        this.rolesId = new LinkedList<>();
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

    public List<String> getRolesId() {
        return rolesId;
    }

    public void setRolesId(List<String> rolesId) {
        this.rolesId = rolesId;
    }

    public Set<Attribute> getAttributes() {
        // TODO automate and simplify
        Set<Attribute> attributeSet = new HashSet<>();

        attributeSet.add(AttributeBuilder.build("userName", this.userName));
        attributeSet.add(AttributeBuilder.build("age", this.age));
        if (this.rolesId.size() > 0){
            attributeSet.add(AttributeBuilder.build("rolesId",this.rolesId.toArray()));
        }

        return attributeSet;
    }
}
