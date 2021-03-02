package com.personal.study.springoauth2server.repositories;

import com.personal.study.springoauth2server.entities.OAuthAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OAuthAccessTokenRepository {

    @Autowired
    private MongoOperations mongoOperations;

    public OAuthAccessToken findByTokenId(final String tokenId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(tokenId));

        return mongoOperations.findOne(query, OAuthAccessToken.class);

    }

    public OAuthAccessToken findByAuthenticationId(String authenticationId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("authenticationId").is(authenticationId));

        return mongoOperations.findOne(query, OAuthAccessToken.class);
    }

    public void save(final OAuthAccessToken oAuthAccessToken) {
        mongoOperations.save(oAuthAccessToken);
    }

    public void removeByAccessTokenId(final String tokenId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(tokenId));

        mongoOperations.remove(query, OAuthAccessToken.class);
    }

    public void removeByRefreshTokenId(final String tokenId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("refreshToken").is(tokenId));

        mongoOperations.remove(query, OAuthAccessToken.class);
    }

    public List<OAuthAccessToken> findByClientIdAndUsername(final String clientId, final String username) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
        query.addCriteria(Criteria.where("username").is(username));

        return mongoOperations.find(query, OAuthAccessToken.class);
    }

    public List<OAuthAccessToken> findByClientId(final String clientId) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));

        return mongoOperations.find(query, OAuthAccessToken.class);
    }
}
