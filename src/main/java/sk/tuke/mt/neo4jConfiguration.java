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

import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class neo4jConfiguration extends AbstractConfiguration {

    private static final Log LOG = Log.getLog(neo4jConfiguration.class);

    private String uri;
    private String userName;
    private String password;

    @Override
    public void validate() {
        if ((uri == null || uri.length() == 0) || (userName == null || userName.length() == 0) ||
                (password == null || password.length() == 0)){
            throw new ConfigurationException("Invalid configuration");
        }
    }

    @ConfigurationProperty(displayMessageKey = "neo4j.config.uri",
            helpMessageKey = "neo4j.config.uri.help")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @ConfigurationProperty(displayMessageKey = "neo4j.config.username",
            helpMessageKey = "neo4j.config.username.help")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ConfigurationProperty(displayMessageKey = "neo4j.config.password",
            helpMessageKey = "neo4j.config.password.help")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}