package sk.tuke.mt.utils;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class PropertyMapper {

    private String propertyName;
    private String propertyNeoType;
    private boolean mandatory;

    public PropertyMapper(String propertyName, String propertyNeoType, boolean mandatory) {
        this.propertyName = propertyName;
        this.propertyNeoType = propertyNeoType;
        this.mandatory = mandatory;
    }

    public boolean isMultivalued(){
        return this.propertyNeoType.toLowerCase().contains("array");
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
        // TODO refactor this method and fix mappings
        Map<String, Class<?>> types = Map.of(
                "String",String.class,
                "Integer", Integer.class,
                "Long", Long.class,
                "Float", Float.class,
                "Double", Double.class,
                "Boolean", Boolean.class,
                "DateTime", ZonedDateTime.class); //TODO other date types ????
        for (String key: types.keySet()){
            if (this.propertyNeoType.contains(key)){
                return types.get(key);
            }
        }
        return null;

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
