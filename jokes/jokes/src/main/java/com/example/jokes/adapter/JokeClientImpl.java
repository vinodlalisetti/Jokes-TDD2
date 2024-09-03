package com.example.jokes.adapter;

import com.example.jokes.core.Joke;
import com.example.jokes.port.JokeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class JokeClientImpl implements JokeClient {

    private WebClient webClient;

    public JokeClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://official-joke-api.appspot.com").build();
    }

    @Override
    public Flux<Joke> fetchJokes(int count) {
        return Flux.range(1, count)
                .flatMap(i -> webClient.get()
                        .uri("/random_joke")
                        .retrieve()
                        .bodyToMono(Joke.class)
                )
                .onErrorResume(e -> {
                    // Log and handle errors if needed
                    System.err.println("Error fetching joke: " + e.getMessage());
                    return Flux.empty();
                });
    }

    // Setter for testing
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
