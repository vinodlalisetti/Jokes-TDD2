// src/main/java/com/example/jokes/port/JokeClient.java
package com.example.jokes.port;

import com.example.jokes.core.Joke;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
@Component
public interface JokeClient {
    Flux<Joke> fetchJokes(int count);
}
