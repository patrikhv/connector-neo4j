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
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.PoolableConnector;
import org.identityconnectors.framework.spi.operations.*;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import sk.tuke.mt.utils.QueryBuilder;
import sk.tuke.mt.utils.Relationship;
import sk.tuke.mt.utils.RelationshipsMapper;
import sk.tuke.mt.utils.SchemaHelper;

import java.util.*;


@ConnectorClass(displayNameKey = "neo4j.connector.display", configurationClass = neo4jConfiguration.class)
public class neo4jConnector implements PoolableConnector, CreateOp, UpdateDeltaOp, DeleteOp, SchemaOp, TestOp, SearchOp<String> {

    private static final Log LOG = Log.getLog(neo4jConnector.class);

    private neo4jConnection connection;

    @Override
    public Configuration getConfiguration() {
        return connection.getConfiguration();
    }

    @Override
    public void init(Configuration configuration) {
        this.connection = new neo4jConnection((neo4jConfiguration) configuration);
    }

    @Override
    public void dispose() {
        if (connection != null) {
            connection.dispose();
        }
    }

    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> set, OperationOptions operationOptions) {
        String id;
        try (Session session = this.connection.getDriver().session()){
            id = session.writeTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.createQuery(objectClass, set));
                return String.valueOf(result.single().get( 0 ).asInt());
            });
        }
        if (id != null) {
            for (Attribute attribute: set){
                Relationship relationship = RelationshipsMapper.getRelationship(attribute);
                if (relationship != null){
                    createRelationship(id,attribute.getValue(),relationship, null);
                }
            }
            return new Uid(id);
        }
        return null;
    }

    public void createRelationship(String uid, List<Object> values, Relationship relationship, List<Object> params){
        String id = null;
        try (Session session = this.connection.getDriver().session()){
            id = session.writeTransaction(transaction -> {
                for (Object val: values){
                    Result result = transaction.run(QueryBuilder.createRelationshipQuery(uid,(String) val, relationship, params));
                }
                return null;
            });
        }
    }

    public void deleteRelationship(String uid, List<Object> values, Relationship relationship){
        try (Session session = this.connection.getDriver().session()){
            session.writeTransaction(transaction -> {
                for (Object val: values){
                    transaction.run(QueryBuilder.deleteRelationshipQuery(uid,(String) val, relationship));
                }
                return null;
            });
        }
    }

    public void deleteAllRelationships(String uid, Relationship relationship){
        try (Session session = this.connection.getDriver().session()){
            session.writeTransaction(transaction -> {
                    transaction.run(QueryBuilder.deleteAllRelationshipQuery(uid, relationship));
                return null;
            });
        }
    }

    public void updateRelationship(String uid, List<Object> values, Relationship relationship){
        deleteAllRelationships(uid,relationship);
        createRelationship(uid,values,relationship,null);
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
        List<Record> entities;
        try(Session session = this.connection.getDriver().session()){
            entities = session.readTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.schemaQueryRel());
                return result.list();
            });
        }
        RelationshipsMapper.getRelationshipsFromSchema(entities);
        RelationshipsMapper.relationshipList.forEach(relationship -> System.out.println(relationship.toString()));
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
    public Set<AttributeDelta> updateDelta(ObjectClass objectClass, Uid uid, Set<AttributeDelta> set, OperationOptions operationOptions) {

        try (Session session = this.connection.getDriver().session()){
            session.writeTransaction(transaction -> {
                Query query = QueryBuilder.updateDeltaQuery(objectClass, uid, set);
                if (query != null){
                    transaction.run(query);
                }
                return null;
            });
        }
        // Relationships update
        for (AttributeDelta attribute: set){
            if (RelationshipsMapper.isVirtualAttributeForRelationship(objectClass,attribute)){
                if (attribute.getValuesToAdd() != null){
                    createRelationship(uid.getUidValue(),attribute.getValuesToAdd(),
                            RelationshipsMapper.getRelationship(attribute),null);
                }
                if (attribute.getValuesToRemove() != null){
                    deleteRelationship(uid.getUidValue(),attribute.getValuesToRemove(),
                            RelationshipsMapper.getRelationship(attribute));
                }
                if (attribute.getValuesToReplace() != null){
                    updateRelationship(uid.getUidValue(),attribute.getValuesToReplace(),
                            RelationshipsMapper.getRelationship(attribute));
                }
            }
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


    @Override
    public FilterTranslator<String> createFilterTranslator(ObjectClass objectClass, OperationOptions operationOptions) {
        return new NeoFilterTranslator();
    }

    @Override
    public void executeQuery(ObjectClass objectClass, String subQuery, ResultsHandler resultsHandler, OperationOptions operationOptions) {
        List<Record> list;
        try (Session session = this.connection.getDriver().session()){
            list = session.readTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.getQuery(objectClass,subQuery,operationOptions));
                return result.list();
            });
        }

        for (Record record :list){
            ConnectorObject connectorObject = buildConnectorObjectFromRecord(record, objectClass);
            resultsHandler.handle(connectorObject);
        }

    }

    private ConnectorObject buildConnectorObjectFromRecord(Record record, ObjectClass objectClass){
        ConnectorObjectBuilder connectorObjectBuilder = new ConnectorObjectBuilder();
        Node node = record.get(0).asNode();
        for (String attributeKey: node.keys()){
            convertTypeAndAdd(connectorObjectBuilder,attributeKey,node);
        }
        addRelationshipsAttributes(objectClass, String.valueOf(node.id()),connectorObjectBuilder);
        connectorObjectBuilder.setUid(new Uid(String.valueOf(node.id())));
        connectorObjectBuilder.setName(new Name("RANDOM NAME"));
        connectorObjectBuilder.setObjectClass(objectClass);
        return connectorObjectBuilder.build();
    }

    private void addRelationshipsAttributes(ObjectClass objectClass,String uid, ConnectorObjectBuilder connectorObjectBuilder){
        List<Record> list;
        try (Session session = this.connection.getDriver().session()){
            list = session.readTransaction(transaction -> {
                Result result = transaction.run(QueryBuilder.getNodeRelationships(objectClass,uid));
                return result.list();
            });
        }
        list.forEach(System.out::println);
        for (Record record: list){
            String relationshipName = record.get("relationship").asString();
            String label = record.get("label").asList().get(0).toString();
            List<String> ids = new ArrayList<>();
            for (Object value: record.get("id").asList()){
                ids.add(String.valueOf(value));
            }
            Relationship relationship = RelationshipsMapper.getRelationshipByName(relationshipName);
            if (relationship != null){
                connectorObjectBuilder.addAttribute(relationship.getVirtualAttributeName(), ids);
            }

        }
    }

    private void convertTypeAndAdd(ConnectorObjectBuilder connectorObjectBuilder, String attributeKey ,Node node){
        // TODO CATCH EXCEPTIONS WITH TYPE CONVERSIONS
        String type = node.get(attributeKey).type().name();
        //System.out.println(type);
        Object value;
        switch (type){
            case "NULL" -> value = null;
            case "LIST", "LIST OF ANY?" -> value = node.get(attributeKey).asList();
            case "MAP" -> value = node.get(attributeKey).asMap();
            case "BOOLEAN" -> value = node.get(attributeKey).asBoolean();
            case "INTEGER" -> value = node.get(attributeKey).asInt();
            case "FLOAT" -> value = node.get(attributeKey).asFloat();
            case "STRING" -> value = node.get(attributeKey).asString();
            case "BYTES" -> value = (Object) node.get(attributeKey).asByteArray();
            case "DATE" -> value = node.get(attributeKey).asLocalDate();
            case "TIME" -> value = node.get(attributeKey).asOffsetTime();
            case "LOCAL_TIME" -> value = node.get(attributeKey).asLocalTime();
            case "DATE_TIME" -> value = node.get(attributeKey).asZonedDateTime();
            case "LOCAL_DATE_TIME" -> value = node.get(attributeKey).asLocalDateTime();
            case "POINT" -> value = node.get(attributeKey).asPoint();
            case "NODE" -> value = node.get(attributeKey).asNode();
            case "RELATIONSHIP" -> value = node.get(attributeKey).asRelationship();
            case "PATH" -> value = node.get(attributeKey).asPath();
            default -> value = node.get(attributeKey).asObject();
        }
        if (value != null){
            // We need to change SingletonList for standard list
            if (value instanceof List){
                connectorObjectBuilder.addAttribute(attributeKey, new ArrayList<>((List<?>) value));
                return;
            }
            connectorObjectBuilder.addAttribute(attributeKey, value);

        }
    }
}
