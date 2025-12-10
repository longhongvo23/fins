package com.stockapp.userservice.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import tech.jhipster.config.JHipsterProperties;

/**
 * JWT Token Provider for authentication
 * Handles token generation, validation and claims extraction
 */
@Component
public class TokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final Key key;
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (secret != null && !secret.isEmpty()) {
            LOG.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            LOG.warn(
                    "Warning: the JWT key used is not Base64-encoded. " +
                            "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes();
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000
                * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe = 1000 * jHipsterProperties.getSecurity().getAuthentication()
                .getJwt().getTokenValidityInSecondsForRememberMe();
    }

    /**
     * Create JWT token from Authentication
     * 
     * @param authentication Spring Security authentication object
     * @param rememberMe     whether to extend token validity
     * @return JWT token string
     */
    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * Create JWT token from username and authorities
     * 
     * @param username    user login name
     * @param authorities list of user authorities/roles
     * @param rememberMe  whether to extend token validity
     * @return JWT token string
     */
    public String createToken(String username, Collection<String> authorities, boolean rememberMe) {
        String authoritiesStr = String.join(",", authorities);

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
                .builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authoritiesStr)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .compact();
    }

    /**
     * Get Authentication from JWT token
     * 
     * @param token JWT token
     * @return Spring Security Authentication object
     */
    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(authority -> (GrantedAuthority) () -> authority)
                .collect(Collectors.toList());

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
                claims.getSubject(),
                "",
                authorities);

        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, token,
                authorities);
    }

    /**
     * Validate JWT token
     * 
     * @param authToken JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            LOG.trace(INVALID_JWT_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            LOG.trace(INVALID_JWT_TOKEN, e);
        } catch (MalformedJwtException e) {
            LOG.trace(INVALID_JWT_TOKEN, e);
        } catch (SignatureException e) {
            LOG.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            LOG.error("Token validation error {}", e.getMessage());
        }
        return false;
    }

    /**
     * Extract username from JWT token
     * 
     * @param token JWT token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Extract authorities from JWT token
     * 
     * @param token JWT token
     * @return collection of authority strings
     */
    public Collection<String> getAuthoritiesFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        String authoritiesStr = claims.get(AUTHORITIES_KEY, String.class);
        if (authoritiesStr == null || authoritiesStr.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(authoritiesStr.split(","));
    }

    /**
     * Get token expiration date
     * 
     * @param token JWT token
     * @return expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    /**
     * Check if token is expired
     * 
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Get token validity duration in seconds
     * 
     * @param rememberMe whether using remember me functionality
     * @return validity duration in seconds
     */
    public long getTokenValidityInSeconds(boolean rememberMe) {
        if (rememberMe) {
            return this.tokenValidityInMillisecondsForRememberMe / 1000;
        } else {
            return this.tokenValidityInMilliseconds / 1000;
        }
    }
}
