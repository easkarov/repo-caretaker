package ru.tinkoff.edu.java.bot.bot;

import lombok.Getter;

public enum Command {
    START("start", "Start bot"),
    TRACK("track", "Track link"),
    HELP("help", "List all commands");

    @Getter
    private final String name, description;

    Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String command() {
        return "/%s".formatted(name);
    }

    @Override
    public String toString() {
        return "%s - %s".formatted(command(), description);
    }
}
