package com.ema.secondbrain.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import javax.naming.AuthenticationException;

public class CustomAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }

}
