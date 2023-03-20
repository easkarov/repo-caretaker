package ru.tinkoff.edu.java;

import ru.tinkoff.edu.java.parser.BaseParser;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.response.BaseResponse;

import java.util.Optional;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        // P.s. Знаю, что класс бесполезный :) Но проверить же как-то нужно было

        var parser = BaseParser.chain(new GitHubParser(), new StackOverflowParser());

        while (true) {
            String url = new Scanner(System.in).nextLine();
            Optional<BaseResponse> response = parser.parse(url);
            if (response.isPresent()) {
                System.out.println(response);
            } else {
                System.out.println("Can't process this url.");
            }
        }
    }
}
