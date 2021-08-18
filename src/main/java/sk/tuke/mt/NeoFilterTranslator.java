package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.filter.*;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.common.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeoFilterTranslator extends AbstractFilterTranslator<String> {
    //https://github.com/Tirasa/ConnIdDBBundle/blob/master/scriptedsql/src/main/java/net/tirasa/connid/bundles/db/scriptedsql/ScriptedSQLFilterTranslator.java

    private Object prepareValue(List<Object> attributeValue){
        List<Object> results = new ArrayList<>();
        for (Object object : attributeValue) {
            if (object instanceof String) {
                // TODO other data types convert
                results.add("'" + object + "'");
            } else {
                results.add(object);
            }
        }
        Object result;
        if (results.size() == 1)
            result = results.get(0);
        else
            result = results;
        return result;
    }


    //TODO check types before

    @Override
    protected String createEqualsExpression(EqualsFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        if (attributeName.equals("Uid")){
            return String.format(
                    "id(n) %s %s", not?"!=":"=",prepareValue(attributeValue)
            );
        }else {
            return String.format(
                    "n.%s %s %s",attributeName, not?"!=":"=", prepareValue(attributeValue)
            );
        }

    }

    @Override
    protected String createEqualsIgnoreCaseExpression(EqualsIgnoreCaseFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "LOWER n.%s %s LOWER %s",attributeName, not?"!=":"=", prepareValue(attributeValue)
        );
    }

    @Override
    protected String createContainsExpression(ContainsFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s CONTAINS %s", attributeName, prepareValue(attributeValue)
        );
    }

    @Override
    protected String createEndsWithExpression(EndsWithFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s ENDS WITH %s", attributeName, prepareValue(attributeValue)
        );
    }

    @Override
    protected String createStartsWithExpression(StartsWithFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s STARTS WITH %s", attributeName, prepareValue(attributeValue)
        );
    }

    @Override
    protected String createGreaterThanExpression(GreaterThanFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s > %s", attributeName, prepareValue(attributeValue)
        );
    }

    @Override
    protected String createGreaterThanOrEqualExpression(GreaterThanOrEqualFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s >= %s", attributeName, prepareValue(attributeValue)
        );
    }

    @Override
    protected String createLessThanExpression(LessThanFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s < %s", attributeName, prepareValue(attributeValue)
        );
    }

    @Override
    protected String createLessThanOrEqualExpression(LessThanOrEqualFilter filter, boolean not) {
        Attribute attribute = filter.getAttribute();
        String attributeName = attribute.getName();
        List<Object> attributeValue = attribute.getValue();
        return String.format(
                "n.%s <= %s", attributeName, prepareValue(attributeValue)
        );
    }


}
