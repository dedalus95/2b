package com.ema.secondbrain.utilities;

import com.ema.secondbrain.constants.SecurityConstants;
import com.ema.secondbrain.entity.User;
import com.ema.secondbrain.repository.RefreshTokenRepository;
import com.ema.secondbrain.security.entity.RefreshToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
public record JwtUtility(RefreshTokenRepository refreshTokenRepository) {

    @Autowired
    public JwtUtility {
    }

    public String generateToken(String username, Collection<String> roles) {

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()))
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
                .claim("rol", roles)
                .compact();
    }

    /**
     * Check if the given refresh token is valid or expired.
     *
     * @param token the refresh token to be checked.
     * @return true if the token is valid, false otherwise.
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new BadCredentialsException("Refresh token was expired. Please make a new sign in request");
        }

        return token;
    }


    /**
     * Create a new refresh token for the given user.
     * @param user the user for which the refresh token will be created.
     * @return the created refresh token.
     */
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(SecurityConstants.REFRESH_TOKEN_EXPIRATION));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }
}
