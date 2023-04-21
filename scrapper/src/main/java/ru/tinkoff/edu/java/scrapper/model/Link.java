package ru.tinkoff.edu.java.scrapper.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "link")
@Getter
@Setter
@Accessors(chain = true)
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String url;

    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private String updateData;

    @ManyToMany(mappedBy = "links", fetch = FetchType.LAZY)
    private Set<Chat> chats;

    public boolean removeFromChat(Chat chat) {
        return this.getChats().remove(chat) && chat.getLinks().remove(this);
    }

    public boolean addToChat(Chat chat) {
        return this.getChats().add(chat) && chat.getLinks().add(this);
    }

}
