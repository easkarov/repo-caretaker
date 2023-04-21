package ru.tinkoff.edu.java.scrapper.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "chat")
@Data
@Accessors(chain = true)
public class Chat {

    @Id
    private long id;

    @ManyToMany(mappedBy = "chats")
    private List<Link> links;

}
