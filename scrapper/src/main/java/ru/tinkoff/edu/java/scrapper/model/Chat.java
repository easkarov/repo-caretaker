package ru.tinkoff.edu.java.scrapper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "chat")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Chat {

    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "chat_link",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<Link> links = new HashSet<>();

}
