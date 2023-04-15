package ru.tinkoff.edu.java.scrapper.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime updatedAt;
    private String updateData;
}
