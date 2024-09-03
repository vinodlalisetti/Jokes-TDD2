package com.example.jokes.core;

import com.example.jokes.port.JokeClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class JokeServiceImpl implements JokeService {

    private final JokeClient jokeClient;

    public JokeServiceImpl(JokeClient jokeClient) {
        this.jokeClient = jokeClient;
    }

    @Override
    public Flux<Joke> getJokes(int count) {
        // Determine the number of batches needed
        int batches = (count + 9) / 10;

        // Create a Flux that emits the results from all batches
        return Flux.range(0, batches)
                .flatMap(i -> jokeClient.fetchJokes(10)) // Fetch each batch of jokes
                .take(count); // Take only the requested count of jokes
    }
}
