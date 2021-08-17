package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.filter.*;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.common.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class NeoFilterTranslator extends AbstractFilterTranslator<Map<String,Object>> {
    //https://github.com/Tirasa/ConnIdDBBundle/blob/master/scriptedsql/src/main/java/net/tirasa/connid/bundles/db/scriptedsql/ScriptedSQLFilterTranslator.java

    private Map<String,Object> createMap(String operation, AttributeFilter filter, boolean not){
        Map<String, Object> map = new HashMap<String, Object>();
        String name = filter.getAttribute().getName();
        String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        } else {
            map.put("not", not);
            map.put("operation", operation);
            map.put("left", name);
            map.put("right", value);
            return map;
        }
    }


    @Override
    protected Map<String,Object> createEqualsExpression(EqualsFilter filter, boolean not) {
        return createMap("EQUALS", filter, not);
    }

    @Override
    protected Map<String, Object> createEqualsIgnoreCaseExpression(EqualsIgnoreCaseFilter filter, boolean not) {
        return createMap("EQUALSIGNORECASE", filter, not);
    }
}
