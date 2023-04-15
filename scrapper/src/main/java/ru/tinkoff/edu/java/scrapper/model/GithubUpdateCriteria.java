package ru.tinkoff.edu.java.scrapper.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class GithubUpdateCriteria {
    private Integer commitsNumber;

}
