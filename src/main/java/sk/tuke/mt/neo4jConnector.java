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
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.PoolableConnector;
import org.identityconnectors.framework.spi.operations.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import sk.tuke.mt.utils.QueryBuilder;
import sk.tuke.mt.utils.SchemaHelper;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@ConnectorClass(displayNameKey = "neo4j.connector.display", configurationClass = neo4jConfiguration.class)
public class neo4jConnector implements PoolableConnector, CreateOp, UpdateOp, DeleteOp, SchemaOp, TestOp {

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

    //TODO relationships
    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> set, OperationOptions operationOptions) {
        String id = null;
        try (Session session = this.connection.getDriver().session()){
            id = session.writeTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.createQuery(objectClass, set));
                return String.valueOf(result.single().get( 0 ).asInt());
            });
        }
        if (id != null) {
            System.out.printf("New object in Neo4j with id: %s%n", id );
            for (Attribute attribute: set){
                if (attribute.getName().equals("rolesId")){
                    createRelationship(id,attribute.getValue(),"IS_MEMBER_OF", null);
                }
            }
            return new Uid(id);
        }
        return null;
    }

    public void createRelationship(String uid, List<Object> values, String name, List<Object> params){
        String id = null;
        try (Session session = this.connection.getDriver().session()){
            id = session.writeTransaction(transaction -> {
                for (Object val: values){
                    Result result = transaction.run(QueryBuilder.createRelationshipQuery(uid,(String) val, name, params));
                }
                return null;
            });
        }
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions operationOptions) {
        try(Session session = this.connection.getDriver().session()){
            session.writeTransaction(transaction -> {
                transaction.run(QueryBuilder.deleteQuery(objectClass, uid));
                return null;
            });
        }
    }

    @Override
    public Schema schema() {
        // TODO what to do with relationships?
        List<Record> records;
        try(Session session = this.connection.getDriver().session()){
            records = session.readTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.schemaQueryAll());
                return result.list();
            });
        }
        return SchemaHelper.getSchema(records);
    }

    @Override
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> set, OperationOptions operationOptions) {
        // TODO update of relationships
        String id = null;
        try (Session session = this.connection.getDriver().session()){
            id = session.writeTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.updateQuery(objectClass, uid, set));
                return String.valueOf(result.single().get( 0 ).asInt());
            });
        }
        if (id != null) {
            return new Uid(id);
        }
        return null;
    }

    @Override
    public void test() {
        LOG.info("Connection test: started");
        cleanupBeforeTest();
        connection.connect();
        try{
            connection.getDriver().verifyConnectivity();
            LOG.info("Connection test: finished");
        } catch (Exception e){
            LOG.error("Connection test: failed!");
            connection.dispose();
            throw new ConnectionFailedException(e);
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
            throw new ConnectionFailedException(e);
        }
    }
}
