package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;

public class NeoResultHandler implements ResultsHandler {
    @Override
    public boolean handle(ConnectorObject connectorObject) {
        System.out.println(connectorObject.toString());
        return false;
    }
}
