package ru.tinkoff.edu.java.scrapper.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class Link {
    private Long id;
    private String url;
    private Instant updatedAt;
}
