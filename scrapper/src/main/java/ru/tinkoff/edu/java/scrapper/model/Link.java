package ru.tinkoff.edu.java.scrapper.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;


@Entity
@Table(name = "link")
@Data
@Accessors(chain = true)
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String url;

    private OffsetDateTime updatedAt;

    private String updateData;

    @ManyToMany
    private List<Chat> chats;

}
