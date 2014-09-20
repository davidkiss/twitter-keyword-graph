package com.kaviddiss.keywords.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by david on 2014-09-17.
 */
@NodeEntity
public class Keyword {
    @GraphId
    Long id;

    @Indexed(unique=true)
    public String word;
    public int count;

//    @RelatedTo(elementClass = Keyword.class, type = "Connected", direction = Direction.BOTH)
//    private final Set<Keyword> connectedTo = new LinkedHashSet<Keyword>();

}
