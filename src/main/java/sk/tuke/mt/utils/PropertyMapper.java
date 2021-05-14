package sk.tuke.mt.utils;

import java.lang.reflect.Array;
import java.util.List;

public class PropertyMapper {

    private String propertyName;
    private String propertyNeoType;
    private boolean mandatory;

    public PropertyMapper(String propertyName, String propertyNeoType, boolean mandatory) {
        this.propertyName = propertyName;
        this.propertyNeoType = propertyNeoType;
        this.mandatory = mandatory;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyNeoType() {
        return propertyNeoType;
    }

    public void setPropertyNeoType(String propertyNeoType) {
        this.propertyNeoType = propertyNeoType;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Class<?> getPropertyJavaType(){
        // TODO set neo4j db not to generate arrays for single values
        // TODO refactor this method and fix mappings
        System.out.println(this.getPropertyNeoType());
        return switch (this.propertyNeoType) {
            case "StringArray" -> String.class; //
            case "String" -> String.class;
            case "Integer" -> Long.class;
            case "LongArray" -> Long.class;
            case "Boolean" -> Boolean.class;
            default -> Object.class;
        };
    }

    @Override
    public String toString() {
        return "PropertyMapper{" +
                "propertyName='" + propertyName + '\'' +
                ", propertyNeoType='" + propertyNeoType + '\'' +
                ", mandatory=" + mandatory +
                '}';
    }
}
