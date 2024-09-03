// src/main/java/com/example/jokes/core/Joke.java
package com.example.jokes.core;

public class Joke {
    private String setup;
    private String punchline;

    // Constructors, Getters, and Setters
    public Joke() {}

    public Joke(String setup, String punchline) {
        this.setup = setup;
        this.punchline = punchline;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getPunchline() {
        return punchline;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    @Override
    public String toString() {
        return setup + " - " + punchline;
    }
}
