package com.personal.study.springoauth2server.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@Document("client_details")
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientDetails implements ClientDetails {

    @Id
    private String id;
    private Integer accessTokenValiditySeconds;
    private Map<String, Object> additionalInformation;
    private Collection<GrantedAuthority> authorities;
    private Set<String> authorizedGrantTypes;
    private boolean autoApprove;
    private String clientId;
    private String clientSecret;
    private Integer refreshTokenValiditySeconds;
    private Set<String> registeredRedirectUri;
    private Set<String> resourceIds;
    private Set<String> scope;
    private boolean scoped;
    private boolean secretRequired;

    @Override
    public boolean isAutoApprove(String scope) {
        return this.scope.contains(scope);
    }
}
