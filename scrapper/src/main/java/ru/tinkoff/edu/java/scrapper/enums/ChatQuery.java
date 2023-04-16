package ru.tinkoff.edu.java.scrapper.enums;


import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum ChatQuery {

    SELECT_ALL("""
            SELECT * FROM chat
            """),

    SELECT_BY_ID("""
            SELECT * FROM chat WHERE id = ?
            """),

    INSERT("""
            INSERT INTO chat(id) VALUES(?)
            """),

    SELECT_BY_LINK("""
            SELECT chat.* FROM chat JOIN chat_link ON chat.id = chat_id
            WHERE link_id = ?
            """),

    REMOVE_BY_ID("""
                DELETE FROM chat WHERE id = ?
            """);

    private final String query;

    ChatQuery(String query) {
        this.query = query;
    }


}
