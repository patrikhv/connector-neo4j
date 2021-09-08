package sk.tuke.mt;

import org.testng.annotations.Test;

public class TestOpTest extends BaseTest{

    @Test
    public void testGoodConfiguration(){
        Neo4jConnector connector = createConnector();
        connector.test();
        connector.dispose();
    }
}
