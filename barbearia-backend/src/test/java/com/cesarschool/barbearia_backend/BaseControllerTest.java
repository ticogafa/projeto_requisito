package com.cesarschool.barbearia_backend;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;
        
    @BeforeEach
    public abstract void setUp();

    @AfterEach
    public abstract void cleanup();

    public <T> String stringfy(T object) throws Exception{
        return mapper.writeValueAsString(object);
    }

    public <T> T load(String stringObj, Class<T> clazz) throws Exception {
        return mapper.readValue(stringObj, clazz);
    }

}