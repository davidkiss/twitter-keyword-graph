package com.kaviddiss.keywords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude={TwitterAutoConfiguration.class})
@EnableNeo4jRepositories(basePackages = "com.kaviddiss.keywords.repository")
@Import(RepositoryRestMvcConfiguration.class)
public class Application extends Neo4jConfiguration {

    public Application() {
        setBasePackage("com.kaviddiss.keywords.domain");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
