package ru.tinkoff.edu.java.bot.dto;

import java.net.URI;
import java.util.List;


class Parent {
    void method() {
        System.out.println("parent");
    }

}

class Child extends Parent {
    void method() {
        System.out.println("child");
    }

    void check() {
        this.method();
        super.method();
    }

    public static void main(String[] args) {
        new Child().check();
    }

}



public record LinkUpdate(
    long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {
}
