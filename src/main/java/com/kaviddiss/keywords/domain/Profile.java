package com.kaviddiss.keywords.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Created by david on 2014-11-06.
 */
@NodeEntity
public class Profile {
    @GraphId
    Long id;

    @Indexed(unique=true)
    public String screenName;

    public Profile() {
    }

    public Profile(String screenName) {
        this.screenName = screenName;
    }
}
