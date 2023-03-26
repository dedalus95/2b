package com.ema.secondbrain.controller;


import com.ema.secondbrain.constants.Endpoints;
import com.ema.secondbrain.entity.Cat;
import com.ema.secondbrain.service.CatServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.ema.secondbrain.constants.Endpoints.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = CatControllerImpl.class)
@ExtendWith(SpringExtension.class)
public class CatControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CatServiceImpl catService;

    private Cat testCat;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllCats() throws Exception {
        mockMvc.perform(get(API + CATS).contentType(MediaType.APPLICATION_JSON))
                //and expect a response entity empty list
                .andExpect(status().isOk());
    }

    @Test
    public void getCatById() throws Exception {
        mockMvc.perform(get(API + CATS + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void createCat() throws Exception {
        Cat cat = new Cat();
        cat.setName("test");
        cat.setAge(1);
        cat.setBreed("test");
        cat.setColor("test");
        String jsonRequest = mapper.writeValueAsString(cat);

        mockMvc.perform(post(API + CATS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
