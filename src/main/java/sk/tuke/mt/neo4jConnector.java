/*
 * Copyright (c) 2010-2014 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.tuke.mt;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.PoolableConnector;
import org.identityconnectors.framework.spi.operations.*;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import sk.tuke.mt.utils.QueryBuilder;

import java.util.Set;

import static org.neo4j.driver.Values.parameters;

@ConnectorClass(displayNameKey = "neo4j.connector.display", configurationClass = neo4jConfiguration.class)
public class neo4jConnector implements PoolableConnector, CreateOp, UpdateOp,DeleteOp, SchemaOp, TestOp {

    private static final Log LOG = Log.getLog(neo4jConnector.class);

    private neo4jConfiguration configuration;
    private neo4jConnection connection;

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(Configuration configuration) {
        this.configuration = (neo4jConfiguration)configuration;
        this.connection = new neo4jConnection(this.configuration);
        test();
    }

    @Override
    public void dispose() {
        configuration = null;
        if (connection != null) {
            connection.dispose();
            connection = null;
        }
    }

    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> set, OperationOptions operationOptions) {
        try {
            Session session = connection.getDriver().session();
            String txQuerry = "";
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                Result result =
                        transaction.run(QueryBuilder.createQuery(objectClass,set));
                return null; //return result
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Uid(""); //todo return value
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions operationOptions) {

    }

    @Override
    public Schema schema() {
        return null;
    }

    @Override
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> set, OperationOptions operationOptions) {
        return null;
    }

    @Override
    public void test() {

        LOG.info("Connection test: started");
        cleanupBeforeTest();
        connection.connect();
        try{
            // Note: Even if this method throws an exception,
            // the driver still need to be closed via close() to free up all resources.
            connection.getDriver().verifyConnectivity();
            LOG.info("Connection test: finished");
        } catch (Exception e){
            LOG.error("Connection test: failed!");
            connection.dispose();
            throw new RuntimeException(e.getCause());
        }
    }

    protected void cleanupBeforeTest(){
        try {
            LOG.ok("Closing connection ... to reopen them again");
            connection.dispose();
        }catch (Exception e){
            LOG.error("Connection test: failed!");
        }
    }

    @Override
    public void checkAlive() {
        try{
            connection.getDriver().verifyConnectivity();
        }catch (Exception e){
            LOG.error("Check alive: failed! Reconnecting");
            //dispose(); //TODO what if i delete configuration and want to init again
            init(configuration);
        }
    }
}
