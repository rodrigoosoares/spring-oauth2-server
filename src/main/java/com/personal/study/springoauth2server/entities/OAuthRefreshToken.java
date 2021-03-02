package com.personal.study.springoauth2server.entities;

import com.personal.study.springoauth2server.converters.SerializableObjectConverter;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("refresh_token")
public class OAuthRefreshToken {

    private String authenticationSerialized;
    private String tokenId;
    private String tokenSerialized;

    public void setAuthentication(final OAuth2Authentication authentication) {
        this.authenticationSerialized = SerializableObjectConverter.serialize(authentication);
    }

    public OAuth2Authentication getAuthentication() {
        return (OAuth2Authentication) SerializableObjectConverter.deserialize(authenticationSerialized);
    }

    public OAuth2RefreshToken getToken() {
        return (OAuth2RefreshToken) SerializableObjectConverter.deserialize(tokenSerialized);
    }

    public void setToken(final OAuth2RefreshToken token) {
        this.tokenSerialized = SerializableObjectConverter.serialize(token);
    }

}
