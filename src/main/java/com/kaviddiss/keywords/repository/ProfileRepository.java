package com.kaviddiss.keywords.repository;

import com.kaviddiss.keywords.domain.Profile;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by david on 2014-09-16.
 */
@RepositoryRestResource(collectionResourceRel = "profiles", path = "profiles")
public interface ProfileRepository extends GraphRepository<Profile>, RelationshipOperationsRepository<Profile> {
    Profile findByScreenName(String screenName);
}
