package com.personal.study.springoauth2server.repositories;

import com.personal.study.springoauth2server.entities.OAuthClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OAuthClientMongoRepository {

    @Autowired
    private MongoOperations mongoOperations;

    public Optional<OAuthClientDetails> findByClientId(final String clientId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));

        return Optional.ofNullable(mongoOperations.findOne(query, OAuthClientDetails.class));
    }


}
