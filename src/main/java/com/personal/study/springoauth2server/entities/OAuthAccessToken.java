package com.personal.study.springoauth2server.entities;

import com.personal.study.springoauth2server.converters.SerializableObjectConverter;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("access_token")
public class OAuthAccessToken {

    private String authenticationId;
    private String authenticationSerialized;
    private String clientId;
    private String refreshToken;
    private String tokenId;
    private String tokenSerialized;
    private String username;

    public OAuth2Authentication getAuthentication() {
        return (OAuth2Authentication) SerializableObjectConverter.deserialize(authenticationSerialized);
    }

    public void setAuthentication(final OAuth2Authentication authentication) {
        this.authenticationSerialized = SerializableObjectConverter.serialize(authentication);
    }

    public OAuth2AccessToken getToken() {
        return (OAuth2AccessToken) SerializableObjectConverter.deserialize(tokenSerialized);
    }

    public void setToken(final OAuth2AccessToken token) {
        this.tokenSerialized = SerializableObjectConverter.serialize(token);
    }
}
