package sk.tuke.mt;

public abstract class BaseTest {

    private final String neoUri = "bolt://localhost:7687";
    private final String neoUsername = "neo4j";
    private final String neoPassword = "qetuop";

    protected Neo4jConnector connector;


    protected Neo4jConnector createConnector(){
        Neo4jConfiguration configuration = new Neo4jConfiguration();
        configuration.setUri(neoUri);
        configuration.setUserName(neoUsername);
        configuration.setPassword(neoPassword);

        Neo4jConnector connector = new Neo4jConnector();
        connector.init(configuration);
        return connector;
    }
}
