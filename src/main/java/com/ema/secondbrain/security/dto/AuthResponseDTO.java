package com.ema.secondbrain.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponseDTO {

    public long id;
    public String email;
    public String name;
    public Collection<String> roles;
    public String token;
    public String refreshToken;
    public int duration;
}
