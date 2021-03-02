package com.personal.study.springoauth2server.services;

import com.personal.study.springoauth2server.entities.OAuthAccessToken;
import com.personal.study.springoauth2server.entities.OAuthRefreshToken;
import com.personal.study.springoauth2server.repositories.OAuthAccessTokenRepository;
import com.personal.study.springoauth2server.repositories.OAuthRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class MongoTokenStore implements TokenStore {

    // TODO extract to bean
    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    private OAuthAccessTokenRepository accessTokenRepository;

    @Autowired
    private OAuthRefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuthAccessToken accessToken = accessTokenRepository.findByTokenId(extractTokenKey(token));
        return Objects.nonNull(accessToken) ? accessToken.getAuthentication() : null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if(Objects.nonNull(token.getRefreshToken())) {
            refreshToken = token.getRefreshToken().getValue();
        }

        if(Objects.nonNull(readAccessToken(token.getValue()))) {
            removeAccessToken(token);
        }

        OAuthAccessToken mongoTokenToSave = new OAuthAccessToken();
        mongoTokenToSave.setTokenId(extractTokenKey(token.getValue()));
        mongoTokenToSave.setToken(token);
        mongoTokenToSave.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        mongoTokenToSave.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        mongoTokenToSave.setClientId(authentication.getOAuth2Request().getClientId());
        mongoTokenToSave.setAuthentication(authentication);
        mongoTokenToSave.setRefreshToken(extractTokenKey(refreshToken));

        accessTokenRepository.save(mongoTokenToSave);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuthAccessToken accessToken = accessTokenRepository.findByTokenId(extractTokenKey(tokenValue));
        return Objects.nonNull(accessToken) ? accessToken.getToken() : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        accessTokenRepository.removeByAccessTokenId(extractTokenKey(token.getValue()));
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        final OAuthRefreshToken oAuthRefreshToken = new OAuthRefreshToken();
        oAuthRefreshToken.setTokenId(refreshToken.getValue());
        oAuthRefreshToken.setToken(refreshToken);
        oAuthRefreshToken.setAuthentication(authentication);

        refreshTokenRepository.save(oAuthRefreshToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        final OAuthRefreshToken mongoRefreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(tokenValue));
        return Objects.nonNull(mongoRefreshToken) ? mongoRefreshToken.getToken() : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        final OAuthRefreshToken mongoRefreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(token.getValue()));
        return Objects.nonNull(mongoRefreshToken) ? mongoRefreshToken.getAuthentication() : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        refreshTokenRepository.remove(extractTokenKey(token.getValue()));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        accessTokenRepository.removeByRefreshTokenId(extractTokenKey(refreshToken.getValue()));
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);

        OAuthAccessToken mongoAccessToken = accessTokenRepository.findByAuthenticationId(authenticationId);

        if(Objects.nonNull(mongoAccessToken)) {
            accessToken = mongoAccessToken.getToken();

            if(Objects.nonNull(accessToken) && !authenticationId.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken)))) {
                removeAccessToken(accessToken);
                storeAccessToken(accessToken, authentication);
            }
        }

        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AccessToken> tokens = new ArrayList<>();
        List<OAuthAccessToken> savedAccessTokens = accessTokenRepository.findByClientIdAndUsername(clientId, userName);

        savedAccessTokens.forEach(savedAccessToken -> tokens.add(savedAccessToken.getToken()));

        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AccessToken> tokens = new ArrayList<>();
        List<OAuthAccessToken> savedAccessTokens = accessTokenRepository.findByClientId(clientId);

        savedAccessTokens.forEach(savedAccessToken -> tokens.add(savedAccessToken.getToken()));

        return tokens;
    }

    private String extractTokenKey(String value) {
        if(value == null) {
            return null;
        } else {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
            }

            try {
                byte[] e = digest.digest(value.getBytes("UTF-8"));
                return String.format("%032x", new BigInteger(1, e));
            } catch (UnsupportedEncodingException var4) {
                throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
            }
        }
    }
}
