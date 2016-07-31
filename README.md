# twitter-keyword-graph
Sample project that creates graph on words in same tweets using Spring Boot + Spring Social Twitter + Spring Data Neo4J.

To keep things simple, this sample project requires Neo4J authentication to be turned off (not recommended in production).

To turn off authentication, edit the neo4j.conf file and set below flag to false (in Windows, click the Options button in the Neo4J dialog where you can start/stop Neo4J and edit the first file):

```
dbms.security.auth_enabled=false
```

You can also run Neo4J locally with one Docker command (with authentication disabled):
```
docker run --publish=7474:7474 --env=NEO4J_AUTH=none neo4j
```

For more information, see http://kaviddiss.com/2014/12/28/twitter-feed-using-spring-boot/
