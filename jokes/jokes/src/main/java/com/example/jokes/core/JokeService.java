// src/main/java/com/example/jokes/core/JokeService.java
package com.example.jokes.core;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
@Service
public interface JokeService {
    Flux<Joke> getJokes(int count);
}
