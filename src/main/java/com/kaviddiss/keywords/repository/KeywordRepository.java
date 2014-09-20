package com.kaviddiss.keywords.repository;

import com.kaviddiss.keywords.domain.Keyword;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by david on 2014-09-16.
 */
@RepositoryRestResource(collectionResourceRel = "keywords", path = "keywords")
public interface KeywordRepository extends GraphRepository<Keyword>, RelationshipOperationsRepository<Keyword> {
    Keyword findByWord(String word);
}
