package sk.tuke.mt.entity;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String userName;
    private int age;

    public User() {
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
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

    public Set<Attribute> getAttributes() {
        // TODO automate and simplify
        Set<Attribute> attributeSet = new HashSet<>();

        AttributeBuilder builder1 = new AttributeBuilder();
        builder1.setName("userName");
        builder1.addValue(this.userName);

        AttributeBuilder builder2 = new AttributeBuilder();
        builder2.setName("age");
        builder2.addValue(this.age);

        attributeSet.add(builder1.build());
        attributeSet.add(builder2.build());
        return attributeSet;
    }
}
