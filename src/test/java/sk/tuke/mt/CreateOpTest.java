package sk.tuke.mt;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.spi.Connector;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sk.tuke.mt.entity.Project;
import sk.tuke.mt.entity.Role;
import sk.tuke.mt.entity.User;


public class CreateOpTest extends BaseTest{

    @BeforeClass
    public void setUp(){
        this.connector = createConnector();
    }

    @Test
    public void createUserTest(){


    }
}
