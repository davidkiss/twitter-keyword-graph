package com.kaviddiss.keywords.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Created by david on 2014-12-22.
 */
@RelationshipEntity(type = "Author")
public class Author {
    @GraphId
    Long id;
    @StartNode
    public Tweet tweet;
    @EndNode
    public Profile profile;
}