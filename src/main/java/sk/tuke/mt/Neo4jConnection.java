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
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.AuthTokens;

public class Neo4jConnection {

    private static final Log LOG = Log.getLog(Neo4jConnection.class);

    private final Neo4jConfiguration configuration;
    private Driver driver;

    //TODO list of drivers/connectors?

    public Neo4jConnection(Neo4jConfiguration configuration) {
        this.configuration = configuration;
        connect();
    }

    public void connect() {

        configuration.validate();
        this.driver = GraphDatabase.driver( configuration.getUri(), AuthTokens.basic(configuration.getUserName(),
                configuration.getPassword()));
    }

    public void dispose() {
        driver.close();
        driver = null;
    }

    public Driver getDriver(){
        return driver;
    }

    public Neo4jConfiguration getConfiguration(){
        return configuration;
    }
}