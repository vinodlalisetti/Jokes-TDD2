package com.example.jokes.adapter;

import com.example.jokes.core.Joke;
import com.example.jokes.port.JokeClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JokeClientImplIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JokeClient jokeClient;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080)); // Use a fixed port for WireMock
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        // Stub the API endpoint
        WireMock.stubFor(get(urlEqualTo("/random_joke"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"setup\": \"Why don't scientists trust atoms?\", \"punchline\": \"Because they make up everything!\"}")));

        // Set up the WebClient to use the WireMock server
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        ((JokeClientImpl) jokeClient).setWebClient(webClient); // Ensure this setter exists
    }

    @Test
    public void testFetchJokes_success() {
        Flux<Joke> result = jokeClient.fetchJokes(1);

        // Validate result
        assertEquals(1, result.count().block());
        Joke joke = result.blockFirst();
        assertEquals("Why don't scientists trust atoms?", joke.getSetup());
        assertEquals("Because they make up everything!", joke.getPunchline());
    }
}
