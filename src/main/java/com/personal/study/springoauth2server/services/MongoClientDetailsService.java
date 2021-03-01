package com.personal.study.springoauth2server.services;

import com.personal.study.springoauth2server.entities.OAuthClientDetails;
import com.personal.study.springoauth2server.repositories.OAuthClientMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoClientDetailsService implements ClientDetailsService {

    @Autowired
    private OAuthClientMongoRepository oAuthClientMongoRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<OAuthClientDetails> oAuthClientOptional = oAuthClientMongoRepository.findByClientId(clientId);

        return oAuthClientOptional.orElseThrow(() -> new ClientRegistrationException(clientId));
    }
}
