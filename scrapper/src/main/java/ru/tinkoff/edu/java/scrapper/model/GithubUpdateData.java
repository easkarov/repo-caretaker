package ru.tinkoff.edu.java.scrapper.model;


import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class GithubUpdateData {
    private OffsetDateTime updatedAt;
    private Long commitsNumber;
    private Long openIssues;
}
