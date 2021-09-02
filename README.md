# connector-neo4j
Polygon/ConnId connector for Neo4j graph database

## Prerequisites
* Neo4j graph database
* Java version 11

### Neo4j graph database installation
This connector was developed to implement Neo4j graph database interface into ConnId connector framework. Then connector can be used as a resource for identity management systems, which supports ConnId connectors.
First, you should have access to Neo4j graph database (version 1.4.8 or higher).

* Download Neo4j Desktop from [official webpage]:(https://neo4j.com/download-center/)
* Install database following Neo4j installation guide (You could skip Neo4j registration form)
* Optional: If you have access to remote Neo4j database (cloud, server...) you can just connect to it later and skip this installation.

#### Neo4j graph database creation
If you do not have running database with data, you need to create one and fill with data (Example data will be provided later).

To create a new database:
* After successful installation run Neo4j Desktop
* Create new project and open it
* Create a new DBMS inside new project (possible more than one database in one project) and start it.
* By default, two new databases was created. One is called 'System', which is for containing DBMS. The more important is second database, which are now prepared to store our data.
* To work with database directly, you could use Neo4j Browser UI, which is part of Neo4j Desktop 
* Optional: If you have remote database, you could add this database into your Neo4j Desktop application, and work with it from here. Instead of clicking add 'Local DBMS' into project, click 'Remote connection' and insert credentials and URL.

__Important__: After database creation, go to plugins panel and install plugin 'APOC' into your database. Without this plugin, your database will be not correctly working with connector.

To fill database with data:
* TODO example data

Later you will need a credentials and URL to connect to your running database.
* The default username is 'neo4j'
* The password you have set in database creation process
* URL and port you could find in database details in Neo4j desktop application(connector uses bolt protocol, to communicate with database)
* 'bolt://localhost:7687' - localhost database URL

###  Connector preparation

To use this connector you have 2 possibilities.
* Download connector jar file from URL - TODO: upload connector jar somewhere
* Build connector from source code using Maven tools

To build connector with Maven
* Clone connector-neo4j repository with connector source code
* In folder with connector, use Maven commands
`mvn clean -f pom.xml`
and 
`mvn package -f pom.xml`
* Then connector jar package should be built in target folder and connector is prepared to use with Midpoint.

###  Connector usage with Midpoint
* Follow the connector setup guide https://docs.evolveum.com/midpoint/reference/resources/connector-setup/
* Basically, just put your connector jar file into Midpoint home directory.

