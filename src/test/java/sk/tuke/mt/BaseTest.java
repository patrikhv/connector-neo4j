package sk.tuke.mt;

public abstract class BaseTest {

    private final String neoUri = "bolt://localhost:7687";
    private final String neoUsername = "neo4j";
    private final String neoPassword = "qetuop";


    protected neo4jConnector createConnector(){
        neo4jConfiguration configuration = new neo4jConfiguration();
        configuration.setUri(neoUri);
        configuration.setUserName(neoUsername);
        configuration.setPassword(neoPassword);

        neo4jConnector connector = new neo4jConnector();
        connector.init(configuration);
        return connector;
    }
}
