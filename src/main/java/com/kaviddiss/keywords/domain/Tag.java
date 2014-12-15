package com.kaviddiss.keywords.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Created by david on 2014-09-17.
 */
@RelationshipEntity(type = "Tag")
public class Tag {
    @GraphId
    Long id;
    @StartNode
    Tweet tweet;
    @EndNode
    Keyword keyword;
}