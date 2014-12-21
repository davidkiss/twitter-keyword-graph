package com.kaviddiss.keywords.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;
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
    public Date createdAt = new Date();

    public Keyword() {
    }

    public Keyword(String word) {
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
