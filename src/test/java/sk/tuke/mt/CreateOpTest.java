package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Connector;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import sk.tuke.mt.entity.Project;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;

import java.util.HashSet;
import java.util.Set;


public class CreateOpTest extends BaseTest{

    @BeforeClass
    public void setUp(){
        this.connector = createConnector();
        connector.checkAlive();
    }

    @Test
    public void createUserTest(){
        String userObjectClass = "User";
        ObjectClass objectClass = new ObjectClass(userObjectClass);

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(AttributeBuilder.build("username", "TestName"));
        attributes.add(AttributeBuilder.build("password", "123"));
        attributes.add(AttributeBuilder.build("active", true));
        attributes.add(AttributeBuilder.build("personalNumber", 5421));

        OperationOptions operationOptions = new OperationOptionsBuilder().build();

        Uid uid = connector.create(objectClass,attributes,operationOptions);

        assert uid != null;
        assert uid.toString().length() > 0;
    }
}
