package ru.tinkoff.edu.java;

import ru.tinkoff.edu.java.parser.LinkChainParser;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.response.BaseResponse;

import java.util.Optional;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        // P.s. Знаю, что класс бесполезный :) Но проверить же как-то нужно было
        // Я его удалю потом, чтобы можно было спокойно инжектить как URL, так и цепочку

        var parser = LinkChainParser.chain(new GitHubParser(), new StackOverflowParser());
        var scanner = new Scanner(System.in);
        while (true) {
            var url = scanner.nextLine();
            Optional<BaseResponse> response = parser.parse(url);
            if (response.isPresent()) {
                System.out.println(response);
            } else {
                System.out.println("Can't process this url.");
            }
        }
    }
}
