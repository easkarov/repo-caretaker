package ru.tinkoff.edu.java.scrapper.enums;


import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum LinkQuery {

    INSERT("""
            INSERT INTO link(url) VALUES(?)
            """),

    UPDATE("""
            UPDATE link SET url = ?, update_data = ?::jsonb, updated_at = ? WHERE id = ?
            """),

    SELECT_BY_URL("""
            SELECT * FROM link WHERE url = ?
            """),

    SELECT_BY_ID("""
            SELECT * FROM link WHERE id = ?
            """),

    SELECT_ALL("""
             SELECT * FROM link
            """),

    SELECT_BY_CHAT("""
             SELECT link.* FROM link
             JOIN chat_link ON link.id = link_id
             WHERE chat_id = ?
            """),

    REMOVE_BY_ID("""
               DELETE FROM link WHERE id = ?
            """),

    REMOVE_FROM_CHAT("""
              DELETE FROM chat_link WHERE (chat_id, link_id) = (?, ?)
            """),

    EXISTS_IN_CHAT("""
            SELECT EXISTS(SELECT * FROM chat_link WHERE (chat_id, link_id) = (?, ?))
            """),

    ADD_TO_CHAT("""
            INSERT INTO chat_link VALUES(?, ?)
            """),

    SELECT_LEAST_RECENTLY_UPDATED("""
                    SELECT * FROM link WHERE updated_at < ?
            """);

    private final String query;

    LinkQuery(String query) {
        this.query = query;
    }


    }
