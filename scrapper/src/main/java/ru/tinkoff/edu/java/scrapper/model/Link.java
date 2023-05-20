package ru.tinkoff.edu.java.scrapper.model;


import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "link")
@Getter
@Setter
@Accessors(chain = true)
public class Link {

    @Id
    @GeneratedValue(generator = "link_id_seq")
    @SequenceGenerator(name = "link_id_seq", allocationSize = 1)
    private Long id;

    private String url;

    @CreationTimestamp
    private OffsetDateTime updatedAt;

    @Type(JsonType.class)
    private String updateData = "{}";

    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();

    public boolean removeFromChat(Chat chat) {
        return this.getChats().remove(chat) && chat.getLinks().remove(this);
    }

    public boolean addToChat(Chat chat) {
        return this.getChats().add(chat) && chat.getLinks().add(this);
    }

}
