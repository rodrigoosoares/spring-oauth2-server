package com.personal.study.springoauth2server.repositories;

import com.personal.study.springoauth2server.entities.OAuthRefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class OAuthRefreshTokenRepository {

    @Autowired
    private MongoOperations mongoOperations;


    public void save(final OAuthRefreshToken token) {
        mongoOperations.save(token);
    }

    public OAuthRefreshToken findByTokenId(final String tokenId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(tokenId));

        return mongoOperations.findOne(query, OAuthRefreshToken.class);
    }

    public void remove(final String tokenId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(tokenId));

        mongoOperations.remove(query, OAuthRefreshToken.class);
    }


}
