package com.ema.secondbrain.controller;

import com.ema.secondbrain.entity.User;
import com.ema.secondbrain.repository.UserRepository;
import com.ema.secondbrain.security.controller.AuthController;
import com.ema.secondbrain.security.dto.LoginDto;
import com.ema.secondbrain.utilities.JwtUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    ObjectMapper mapper = new ObjectMapper();


    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtility jwtUtility;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void login() throws Exception {

        LoginDto loginDto = new LoginDto("admin", "admin");
        String jsonRequest = mapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
