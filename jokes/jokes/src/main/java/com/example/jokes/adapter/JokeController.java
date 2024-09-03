package com.example.jokes.adapter;

import com.example.jokes.core.Joke;
import com.example.jokes.core.JokeService;
import com.example.jokes.exception.InvalidCountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class JokeController {

    private final JokeService jokeService;

    @Autowired
    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping("/jokes")
    public Mono<ResponseEntity<?>> getJokes(@RequestParam int count) {
        if (count <= 0) {
            throw new InvalidCountException("Count must be greater than zero");
        }
        if (count > 10) {
            throw new InvalidCountException("Count should not be greater than 10");
        }

        return jokeService.getJokes(count)
                .collectList()
                .map(jokes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(new JokeResponse(jokes, "Jokes retrieved successfully")));
    }

    // Wrapper class for response
    public static class JokeResponse {
        private final List<Joke> jokes;
        private final String message;

        public JokeResponse(List<Joke> jokes, String message) {
            this.jokes = jokes;
            this.message = message;
        }

        public List<Joke> getJokes() {
            return jokes;
        }

        public String getMessage() {
            return message;
        }
    }
}
