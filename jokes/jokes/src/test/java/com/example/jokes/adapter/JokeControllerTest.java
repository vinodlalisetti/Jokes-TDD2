package com.example.jokes.adapter;

import com.example.jokes.core.Joke;
import com.example.jokes.core.JokeService;
import com.example.jokes.exception.InvalidCountException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import reactor.core.publisher.Flux;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(JokeController.class)
public class JokeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JokeService jokeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetJokes_invalidCount_zero() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/jokes")
                .param("count", "0")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string("Count must be greater than zero"));
    }

    @Test
    public void testGetJokes_invalidCount_tooHigh() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/jokes")
                .param("count", "11")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string("Count should not be greater than 10"));
    }

    @Test
    public void testGetJokes_validCount() throws Exception {
        // Arrange
        Joke joke1 = new Joke("Why did the scarecrow win an award?", "Because he was outstanding in his field.");
        Joke joke2 = new Joke("I told my wife she was drawing her eyebrows too high.", "She looked surprised.");
        Flux<Joke> jokeFlux = Flux.just(joke1, joke2);

        // Mock service to return jokes
        given(jokeService.getJokes(2)).willReturn(jokeFlux);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/jokes")
                .param("count", "2")
                .accept(MediaType.APPLICATION_JSON));

        // Create the expected response
        String expectedMessage = "Jokes retrieved successfully";
        String expectedResponse = objectMapper.writeValueAsString(new JokeController.JokeResponse(
                Arrays.asList(joke1, joke2), expectedMessage
        ));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse));
    }

}
