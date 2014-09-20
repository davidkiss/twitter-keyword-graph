package com.kaviddiss.keywords.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Created by david on 2014-09-17.
 */
@RelationshipEntity(type = "Connected")
public class Connected {
    @GraphId
    Long id;
    @StartNode
    Keyword start;
    @EndNode
    Keyword end;
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}