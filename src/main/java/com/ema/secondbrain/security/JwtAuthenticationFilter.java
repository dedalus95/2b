package com.ema.secondbrain.security;

import com.ema.secondbrain.constants.SecurityConstants;
import com.ema.secondbrain.exception.ExpiredJwtException;
import com.ema.secondbrain.exception.MalformedJwtException;
import com.ema.secondbrain.exception.UnsupportedJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (!ObjectUtils.isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

                //parse the token.
                Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

                String username = parsedToken
                        .getBody()
                        .getSubject();

                List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                        .get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .toList();


                if (!ObjectUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (ExpiredJwtException exception) {
                logger.warn("Request to parse expired JWT : {} failed : {}".concat(token).concat(exception.getMessage()));
            } catch (UnsupportedJwtException exception) {
                logger.warn("Request to parse unsupported JWT : {} failed : {}".concat(token).concat(exception.getMessage()));
            } catch (MalformedJwtException exception) {
                logger.warn("Request to parse invalid JWT : {} failed : {}".concat(token).concat(exception.getMessage()));
            } catch (IllegalArgumentException exception) {
                logger.warn("Request to parse empty or null JWT : {} failed : {}".concat(token).concat(exception.getMessage()));
            }
        }

        return null;
    }
}
