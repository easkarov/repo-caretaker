package ru.tinkoff.edu.java.scrapper.model;


import lombok.Data;

import java.time.Instant;
import java.time.OffsetDateTime;

@Data
public class GithubUpdateData {
    private Instant updatedAt;
    private Integer commitsNumber;
}
