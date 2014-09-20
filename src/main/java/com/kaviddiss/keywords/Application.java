package com.kaviddiss.keywords;

import com.kaviddiss.keywords.service.TwitterStreamIngester;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude={TwitterAutoConfiguration.class})
@EnableNeo4jRepositories(basePackages = "com.kaviddiss.keywords.repository")
@Import(RepositoryRestMvcConfiguration.class)
public class Application extends Neo4jConfiguration {

    public Application() {
        setBasePackage("com.kaviddiss.keywords.domain");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
//        return new GraphDatabaseFactory().newEmbeddedDatabase("twitter.db");
    }

    @Bean
    public Twitter twitter(final @Value("${spring.social.twitter.appId}") String appId,
                           final @Value("${spring.social.twitter.appSecret}") String appSecret,
                           final @Value("${spring.social.twitter.accessToken}") String accessToken,
                           final @Value("${spring.social.twitter.accessTokenSecret}") String accessTokenSecret) {

        // init twitter4j
        System.setProperty("twitter4j.oauth.consumerKey", appId);
        System.setProperty("twitter4j.oauth.consumerSecret", appSecret);
        System.setProperty("twitter4j.oauth.accessToken", accessToken);
        System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);
        return new TwitterTemplate(appId, appSecret, accessToken, accessTokenSecret);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
